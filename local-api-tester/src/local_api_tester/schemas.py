"""Pydantic 数据结构定义"""
from pydantic import BaseModel, ConfigDict, Field


# ========== 辅助类（Support）==========


class DeviceStats(BaseModel):
    """设备统计摘要"""

    model_config = ConfigDict(from_attributes=True)

    total: int = Field(0, description="设备总数")
    online: int = Field(0, description="在线设备数")
    offline: int = Field(0, description="离线设备数")
    abnormal: int = Field(0, description="异常设备数")


# ========== 请求类（Request）==========


class LoginRequest(BaseModel):
    """登录请求"""

    username: str = Field(..., min_length=1, description="用户名")
    password: str = Field(..., min_length=1, description="密码")


class ApiTestRequest(BaseModel):
    """API 测试请求"""

    base_url: str = Field(..., description="后端服务地址")
    username: str = Field("", description="登录用户名")
    password: str = Field("", description="登录密码")
    selected_api_keys: list[str] = Field(default_factory=list, description="选中的 API key 列表")


# ========== 响应类（Response）==========


class ApiDefinitionResponse(BaseModel):
    """API 接口定义"""

    model_config = ConfigDict(from_attributes=True)

    key: str = Field(description="接口唯一标识")
    name: str = Field(description="接口显示名称")
    method: str = Field(description="请求方法")
    path: str = Field(description="请求路径")
    auth_required: bool = Field(description="是否需要登录")
    category: str = Field(description="接口分类")


class ApiTestResultResponse(BaseModel):
    """单个接口测试结果"""

    model_config = ConfigDict(from_attributes=True)

    api_key: str = Field(description="接口标识")
    api_name: str = Field(description="接口名称")
    method: str = Field(description="请求方法")
    url: str = Field(description="请求 URL")
    success: bool = Field(description="是否成功")
    http_status: int | None = Field(None, description="HTTP 状态码")
    response_code: int | None = Field(None, description="后端响应码")
    response_message: str | None = Field(None, description="后端响应消息")
    cost_ms: int = Field(0, description="请求耗时(ms)")
    error_message: str | None = Field(None, description="错误信息")
    summary: DeviceStats | None = Field(None, description="设备摘要")
    request_headers: str | None = Field(None, description="请求头")
    request_body: str | None = Field(None, description="请求体")
    response_body: str | None = Field(None, description="响应体")


class TestRunSummaryResponse(BaseModel):
    """测试批次汇总"""

    model_config = ConfigDict(from_attributes=True)

    id: int = Field(description="批次 ID")
    started_at: str = Field(description="开始时间")
    finished_at: str | None = Field(None, description="结束时间")
    base_url: str = Field(description="后端地址")
    selected_count: int = Field(description="选中接口数")
    success_count: int = Field(description="成功数")
    fail_count: int = Field(description="失败数")
    total_cost_ms: int = Field(description="总耗时(ms)")
