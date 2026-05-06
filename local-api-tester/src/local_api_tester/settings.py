"""应用统一配置入口"""
from pathlib import Path

from pydantic import model_validator
from pydantic_settings import BaseSettings

_PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent


class Settings(BaseSettings):
    base_url: str = "http://localhost:8081"
    timeout_seconds: int = 10
    default_username: str = "admin01"
    default_password: str = "Jzsk@123456"
    database_url: str = "sqlite:///data/api_test_logs.db"

    model_config = {
        "env_prefix": "LOCAL_API_TESTER_",
        "env_file": ".env",
        "env_file_encoding": "utf-8",
    }

    @model_validator(mode="after")
    def _resolve_database_url(self):
        """将相对 SQLite 路径解析为基于项目根目录的绝对路径"""
        if self.database_url.startswith("sqlite:///") and not self.database_url.startswith("sqlite:////"):
            relative_path = self.database_url[len("sqlite:///"):]
            abs_path = (_PROJECT_ROOT / relative_path).as_posix()
            self.database_url = f"sqlite:///{abs_path}"
        return self


settings = Settings()
