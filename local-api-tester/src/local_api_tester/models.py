"""SQLAlchemy 数据库模型定义"""
from sqlalchemy import Column, Integer, SmallInteger, String, Text, ForeignKey
from sqlalchemy.sql import func
from sqlalchemy.orm import DeclarativeBase


class Base(DeclarativeBase):
    pass


class TestRun(Base):
    """测试批次表"""

    __tablename__ = "test_runs"

    id = Column(Integer, primary_key=True, autoincrement=True, comment="主键 ID")
    started_at = Column(Text, nullable=False, comment="测试开始时间")
    finished_at = Column(Text, comment="测试结束时间")
    base_url = Column(Text, nullable=False, comment="后端服务地址")
    selected_count = Column(Integer, nullable=False, default=0, comment="选中接口数")
    success_count = Column(Integer, nullable=False, default=0, comment="成功接口数")
    fail_count = Column(Integer, nullable=False, default=0, comment="失败接口数")
    total_cost_ms = Column(Integer, nullable=False, default=0, comment="总耗时(ms)")
    created_at = Column(Text, nullable=False, server_default=func.strftime("%Y-%m-%d %H:%M:%S", "now"), comment="创建时间")
    updated_at = Column(Text, nullable=False, server_default=func.strftime("%Y-%m-%d %H:%M:%S", "now"), comment="更新时间")


class ApiTestLog(Base):
    """接口测试日志表"""

    __tablename__ = "api_test_logs"

    id = Column(Integer, primary_key=True, autoincrement=True, comment="主键 ID")
    run_id = Column(Integer, ForeignKey("test_runs.id"), nullable=False, index=True, comment="关联测试批次 ID")
    api_key = Column(String(64), nullable=False, comment="接口唯一标识")
    api_name = Column(String(128), nullable=False, comment="接口显示名称")
    method = Column(String(16), nullable=False, comment="请求方法")
    url = Column(Text, nullable=False, comment="请求 URL")
    request_headers = Column(Text, comment="请求头 JSON")
    request_body = Column(Text, comment="请求体")
    http_status = Column(Integer, comment="HTTP 状态码")
    success = Column(SmallInteger, nullable=False, default=0, index=True, comment="是否成功 0=失败 1=成功")
    cost_ms = Column(Integer, nullable=False, default=0, comment="请求耗时(ms)")
    response_code = Column(Integer, comment="后端响应码")
    response_message = Column(Text, comment="后端响应消息")
    response_body = Column(Text, comment="响应体 JSON")
    error_message = Column(Text, comment="错误信息")
    summary_total = Column(Integer, comment="设备总数")
    summary_online = Column(Integer, comment="在线设备数")
    summary_offline = Column(Integer, comment="离线设备数")
    summary_abnormal = Column(Integer, comment="异常设备数")
    created_at = Column(Text, nullable=False, server_default=func.strftime("%Y-%m-%d %H:%M:%S", "now"), comment="创建时间")
    updated_at = Column(Text, nullable=False, server_default=func.strftime("%Y-%m-%d %H:%M:%S", "now"), comment="更新时间")
