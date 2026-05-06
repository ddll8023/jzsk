"""业务流程编排，负责 API 测试执行与日志记录"""
import json
from datetime import datetime
from pathlib import Path

from local_api_tester.api_client import send_request
from local_api_tester.db import SessionLocal, commit_or_rollback
from local_api_tester.exceptions import ErrorCode, ServiceException
from local_api_tester.models import ApiTestLog, TestRun
from local_api_tester.schemas import (
    ApiDefinitionResponse,
    ApiTestRequest,
    ApiTestResultResponse,
    DeviceStats,
)
from local_api_tester.settings import settings


def load_api_definitions() -> list[ApiDefinitionResponse]:
    """从 config/apis.json 加载接口定义列表"""
    config_path = Path(__file__).resolve().parent.parent.parent / "config" / "apis.json"
    with open(config_path, encoding="utf-8") as f:
        raw = json.load(f)
    return [ApiDefinitionResponse(**item) for item in raw]


def create_test_run(session, base_url: str, selected_count: int) -> TestRun:
    """创建测试批次记录"""
    now = _now_str()
    run = TestRun(
        started_at=now,
        finished_at=None,
        base_url=base_url,
        selected_count=selected_count,
        success_count=0,
        fail_count=0,
        total_cost_ms=0,
        created_at=now,
        updated_at=now,
    )
    session.add(run)
    commit_or_rollback(session)
    session.refresh(run)
    return run


def query_test_run_list(limit: int = 50) -> list[TestRunSummaryResponse]:
    """查询最近测试批次列表"""
    session = SessionLocal()
    try:
        rows = (
            session.query(TestRun)
            .order_by(TestRun.started_at.desc())
            .limit(limit)
            .all()
        )
        return [TestRunSummaryResponse.model_validate(r) for r in rows]
    except Exception as e:
        raise ServiceException(ErrorCode.INTERNAL_ERROR, f"查询测试批次失败: {e}")
    finally:
        session.close()


def query_test_log_list(run_id: int) -> list[ApiTestResultResponse]:
    """查询指定批次的接口测试明细"""
    session = SessionLocal()
    try:
        rows = (
            session.query(ApiTestLog)
            .filter(ApiTestLog.run_id == run_id)
            .order_by(ApiTestLog.id.asc())
            .all()
        )
        results: list[ApiTestResultResponse] = []
        for log in rows:
            summary = None
            if log.summary_total is not None:
                summary = DeviceStats(
                    total=log.summary_total,
                    online=log.summary_online or 0,
                    offline=log.summary_offline or 0,
                    abnormal=log.summary_abnormal or 0,
                )
            results.append(ApiTestResultResponse(
                api_key=log.api_key,
                api_name=log.api_name,
                method=log.method,
                url=log.url,
                success=log.success == 1,
                http_status=log.http_status,
                response_code=log.response_code,
                response_message=log.response_message,
                cost_ms=log.cost_ms,
                error_message=log.error_message,
                summary=summary,
                request_body=log.request_body,
                response_body=log.response_body,
            ))
        return results
    except ServiceException:
        raise
    except Exception as e:
        raise ServiceException(ErrorCode.INTERNAL_ERROR, f"查询测试日志失败: {e}")
    finally:
        session.close()


def export_test_results(results: list[ApiTestResultResponse], file_path: str) -> str:
    """将测试结果导出为 JSON 文件"""
    data = [r.model_dump(mode="json") for r in results]
    try:
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
    except Exception as e:
        raise ServiceException(ErrorCode.INTERNAL_ERROR, f"导出失败: {e}")
    return file_path


def execute_selected_apis(request: ApiTestRequest) -> list[ApiTestResultResponse]:
    """执行选中的 API 测试，返回每个接口的测试结果"""
    all_defs = load_api_definitions()
    api_map = {d.key: d for d in all_defs}
    selected_defs = [api_map[k] for k in request.selected_api_keys if k in api_map]

    if not selected_defs:
        raise ServiceException(ErrorCode.PARAM_ERROR, "未选中任何有效接口")

    base_url = request.base_url or settings.base_url
    session = SessionLocal()
    results: list[ApiTestResultResponse] = []

    try:
        run = create_test_run(session, base_url, len(selected_defs))

        # 判断是否需要登录
        needs_auth = any(d.auth_required for d in selected_defs)
        token = None
        login_failed = False

        if needs_auth or "login" in request.selected_api_keys:
            token, login_failed = _do_login(base_url, request.username, request.password)
            login_result = _build_login_result(base_url, token, login_failed)
            results.append(login_result)
            _write_test_log(session, run.id, login_result)
            commit_or_rollback(session)

        # 登录失败时跳过依赖登录态的接口
        if login_failed:
            for d in selected_defs:
                if d.key == "login":
                    continue
                skip_result = _build_skipped_result(d, base_url, "登录失败，未执行请求")
                results.append(skip_result)
                _write_test_log(session, run.id, skip_result)
                commit_or_rollback(session)
        else:
            for d in selected_defs:
                if d.key == "login":
                    continue
                result = _execute_single_api(session, run.id, d, base_url, token)
                results.append(result)

        # 更新批次汇总
        _finalize_run(session, run, results)
    except ServiceException:
        raise
    except Exception as e:
        session.rollback()
        raise ServiceException(ErrorCode.INTERNAL_ERROR, f"测试执行异常: {e}")
    finally:
        session.close()

    return results


# ========== 辅助函数 ==========


def _execute_single_api(
    session, run_id: int, api_def: ApiDefinitionResponse, base_url: str, token: str | None
) -> ApiTestResultResponse:
    """执行单个 API 请求并写入日志"""
    url = base_url.rstrip("/") + api_def.path

    try:
        resp = send_request(
            method=api_def.method,
            base_url=base_url,
            path=api_def.path,
            token=token,
        )
    except ServiceException as e:
        result = ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            cost_ms=0,
            error_message=e.message,
        )
        _write_test_log(session, run_id, result)
        commit_or_rollback(session)
        return result

    result = _parse_response(api_def, url, resp)
    _write_test_log(session, run_id, result)
    commit_or_rollback(session)
    return result


def _do_login(base_url: str, username: str, password: str) -> tuple[str | None, bool]:
    """执行登录请求，返回 (token, 是否失败)"""
    if not username or not password:
        return None, True

    try:
        resp = send_request(
            method="POST",
            base_url=base_url,
            path="/api/auth/login",
            json_body={"username": username, "password": password},
        )
    except ServiceException:
        return None, True

    if resp["http_status"] != 200:
        return None, True

    try:
        body = json.loads(resp["response_body"])
    except (json.JSONDecodeError, TypeError):
        return None, True

    if body.get("code") != 200:
        return None, True

    token = (body.get("data") or {}).get("token")
    return token, token is None


def _parse_response(
    api_def: ApiDefinitionResponse, url: str, resp: dict
) -> ApiTestResultResponse:
    """解析 HTTP 响应，判断成功/失败"""
    http_status = resp["http_status"]
    cost_ms = resp["cost_ms"]
    response_body = resp["response_body"]

    if api_def.key == "health":
        return _parse_health_response(api_def, url, http_status, cost_ms, response_body)

    if api_def.key.startswith("device_"):
        return _parse_device_response(api_def, url, http_status, cost_ms, response_body)

    # 通用 JSON 响应解析
    try:
        body = json.loads(response_body)
    except (json.JSONDecodeError, TypeError):
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            cost_ms=cost_ms,
            error_message="响应不是合法 JSON",
            response_body=response_body,
        )

    code = body.get("code")
    message = body.get("message")
    success = http_status == 200 and code == 200

    return ApiTestResultResponse(
        api_key=api_def.key,
        api_name=api_def.name,
        method=api_def.method,
        url=url,
        success=success,
        http_status=http_status,
        response_code=code,
        response_message=message,
        cost_ms=cost_ms,
        response_body=response_body,
        error_message=None if success else (message or "后端业务失败"),
    )


def _parse_health_response(
    api_def: ApiDefinitionResponse, url: str, http_status: int, cost_ms: int, response_body: str
) -> ApiTestResultResponse:
    """解析健康检查响应"""
    if http_status != 200:
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            cost_ms=cost_ms,
            error_message=f"HTTP 状态码非 200: {http_status}",
            response_body=response_body,
        )

    try:
        body = json.loads(response_body)
    except (json.JSONDecodeError, TypeError):
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            cost_ms=cost_ms,
            error_message="响应不是合法 JSON",
            response_body=response_body,
        )

    status = body.get("status")
    success = status == "UP"

    return ApiTestResultResponse(
        api_key=api_def.key,
        api_name=api_def.name,
        method=api_def.method,
        url=url,
        success=success,
        http_status=http_status,
        cost_ms=cost_ms,
        response_message=status,
        response_body=response_body,
        error_message=None if success else f"健康状态非 UP: {status}",
    )


def _parse_device_response(
    api_def: ApiDefinitionResponse, url: str, http_status: int, cost_ms: int, response_body: str
) -> ApiTestResultResponse:
    """解析设备监测接口响应，从 data.stats 提取设备摘要"""
    if http_status != 200:
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            cost_ms=cost_ms,
            error_message=f"HTTP 状态码非 200: {http_status}",
            response_body=response_body,
        )

    try:
        body = json.loads(response_body)
    except (json.JSONDecodeError, TypeError):
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            cost_ms=cost_ms,
            error_message="响应不是合法 JSON",
            response_body=response_body,
        )

    code = body.get("code")
    message = body.get("message")
    data = body.get("data") or {}

    if code != 200:
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            response_code=code,
            response_message=message,
            cost_ms=cost_ms,
            response_body=response_body,
            error_message=message or "后端业务失败",
        )

    stats = data.get("stats")
    devices = data.get("devices")
    if stats is None or devices is None:
        return ApiTestResultResponse(
            api_key=api_def.key,
            api_name=api_def.name,
            method=api_def.method,
            url=url,
            success=False,
            http_status=http_status,
            response_code=code,
            response_message=message,
            cost_ms=cost_ms,
            response_body=response_body,
            error_message="响应中缺少 data.stats 或 data.devices",
        )

    device_stats = DeviceStats(
        total=stats.get("total", 0),
        online=stats.get("online", 0),
        offline=stats.get("offline", 0),
        abnormal=stats.get("abnormal", 0),
    )

    return ApiTestResultResponse(
        api_key=api_def.key,
        api_name=api_def.name,
        method=api_def.method,
        url=url,
        success=True,
        http_status=http_status,
        response_code=code,
        response_message=message,
        cost_ms=cost_ms,
        response_body=response_body,
        summary=device_stats,
    )


def _build_login_result(base_url: str, token: str | None, login_failed: bool) -> ApiTestResultResponse:
    """构造登录接口测试结果"""
    url = base_url.rstrip("/") + "/api/auth/login"
    if login_failed:
        return ApiTestResultResponse(
            api_key="login",
            api_name="登录检查",
            method="POST",
            url=url,
            success=False,
            error_message="登录失败",
        )
    return ApiTestResultResponse(
        api_key="login",
        api_name="登录检查",
        method="POST",
        url=url,
        success=True,
        response_message="登录成功",
    )


def _build_skipped_result(api_def: ApiDefinitionResponse, base_url: str, reason: str) -> ApiTestResultResponse:
    """构造跳过执行的接口结果"""
    return ApiTestResultResponse(
        api_key=api_def.key,
        api_name=api_def.name,
        method=api_def.method,
        url=base_url.rstrip("/") + api_def.path,
        success=False,
        error_message=reason,
    )


def _write_test_log(session, run_id: int, result: ApiTestResultResponse) -> None:
    """将单条测试结果写入 api_test_logs"""
    now = _now_str()
    log = ApiTestLog(
        run_id=run_id,
        api_key=result.api_key,
        api_name=result.api_name,
        method=result.method,
        url=result.url,
        http_status=result.http_status,
        success=1 if result.success else 0,
        cost_ms=result.cost_ms,
        response_code=result.response_code,
        response_message=result.response_message,
        response_body=result.response_body,
        error_message=result.error_message,
        summary_total=result.summary.total if result.summary else None,
        summary_online=result.summary.online if result.summary else None,
        summary_offline=result.summary.offline if result.summary else None,
        summary_abnormal=result.summary.abnormal if result.summary else None,
        created_at=now,
        updated_at=now,
    )
    session.add(log)


def _finalize_run(session, run: TestRun, results: list[ApiTestResultResponse]) -> None:
    """更新测试批次汇总信息"""
    run.success_count = sum(1 for r in results if r.success)
    run.fail_count = sum(1 for r in results if not r.success)
    run.total_cost_ms = sum(r.cost_ms for r in results)
    run.finished_at = _now_str()
    run.updated_at = _now_str()
    commit_or_rollback(session)


def _now_str() -> str:
    """当前时间字符串"""
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")
