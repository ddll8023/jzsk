"""统一错误码与服务异常定义"""
from enum import IntEnum


class ErrorCode(IntEnum):
    SUCCESS = 0
    PARAM_ERROR = 1001
    DATA_NOT_FOUND = 1002
    NOT_LOGGED_IN = 2001
    TOKEN_EXPIRED = 2002
    PERMISSION_DENIED = 2003
    NETWORK_ERROR = 4001
    INTERNAL_ERROR = 5001
    LOGIN_FAILED = 6001


class ServiceException(Exception):
    def __init__(self, code: int, message: str):
        self.code = code
        self.message = message
        super().__init__(message)
