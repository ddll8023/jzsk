"""HTTP 请求客户端，封装 requests 库"""
import time

import requests

from local_api_tester.exceptions import ErrorCode, ServiceException
from local_api_tester.settings import settings


def send_request(
    method: str,
    base_url: str,
    path: str,
    *,
    headers: dict | None = None,
    json_body: dict | None = None,
    timeout: int | None = None,
    token: str | None = None,
) -> dict:
    """发送 HTTP 请求并返回统一结果

    Returns:
        {"http_status": int, "response_body": str, "cost_ms": int}
    """
    url = base_url.rstrip("/") + path
    request_headers = dict(headers) if headers else {}
    if token:
        request_headers["Authorization"] = f"Bearer {token}"

    effective_timeout = timeout or settings.timeout_seconds

    start = time.monotonic()
    try:
        resp = requests.request(
            method=method,
            url=url,
            headers=request_headers,
            json=json_body,
            timeout=effective_timeout,
        )
    except requests.ConnectionError:
        raise ServiceException(ErrorCode.NETWORK_ERROR, f"连接失败: {url}")
    except requests.Timeout:
        raise ServiceException(ErrorCode.NETWORK_ERROR, f"请求超时: {url}")
    except requests.RequestException as e:
        raise ServiceException(ErrorCode.NETWORK_ERROR, f"请求异常: {e}")

    cost_ms = int((time.monotonic() - start) * 1000)
    return {
        "http_status": resp.status_code,
        "response_body": resp.text,
        "cost_ms": cost_ms,
    }
