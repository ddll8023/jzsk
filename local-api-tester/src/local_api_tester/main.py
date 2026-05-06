"""本地 API 检测工具桌面应用入口"""
import sys

from PySide6.QtWidgets import QApplication

from local_api_tester.db import init_db
from local_api_tester.ui.main_window import MainWindow


def main():
    app = QApplication(sys.argv)
    app.setApplicationName("本地 API 检测工具")

    init_db()

    window = MainWindow()
    window.show()

    sys.exit(app.exec())


if __name__ == "__main__":
    main()
