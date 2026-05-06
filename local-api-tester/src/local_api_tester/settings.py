"""应用统一配置入口"""
from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    base_url: str = "http://localhost:8081"
    timeout_seconds: int = 10
    default_username: str = "admin"
    database_url: str = "sqlite:///data/api_test_logs.db"

    model_config = {
        "env_prefix": "LOCAL_API_TESTER_",
        "env_file": ".env",
        "env_file_encoding": "utf-8",
    }


settings = Settings()
