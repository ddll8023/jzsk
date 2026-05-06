"""SQLite 数据库连接与会话管理"""

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from local_api_tester.settings import settings
from local_api_tester.models import Base

engine = create_engine(
    settings.database_url,
    echo=False,
    connect_args={"check_same_thread": False},
)

SessionLocal = sessionmaker(bind=engine)


def init_db() -> None:
    """初始化数据库表结构"""
    Base.metadata.create_all(bind=engine)


def get_session():
    """获取数据库会话（生成器方式）"""
    session = SessionLocal()
    try:
        yield session
    except Exception:
        session.rollback()
        raise
    finally:
        session.close()


def commit_or_rollback(session) -> None:
    """统一事务提交辅助函数"""
    try:
        session.commit()
    except Exception as e:
        session.rollback()
        from local_api_tester.exceptions import ErrorCode, ServiceException

        raise ServiceException(ErrorCode.INTERNAL_ERROR, f"数据库操作失败: {e}") from e
