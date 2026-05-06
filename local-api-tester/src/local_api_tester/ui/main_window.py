"""PySide6 主窗口，负责界面展示与用户交互"""

import json

from PySide6.QtCore import Qt, QThread, Signal
from PySide6.QtWidgets import (
    QCheckBox,
    QDialog,
    QFileDialog,
    QGridLayout,
    QGroupBox,
    QHBoxLayout,
    QHeaderView,
    QLabel,
    QLineEdit,
    QMainWindow,
    QMessageBox,
    QPushButton,
    QSplitter,
    QTableWidget,
    QTableWidgetItem,
    QTextEdit,
    QVBoxLayout,
    QWidget,
)

from local_api_tester.schemas import ApiDefinitionResponse, ApiTestRequest, ApiTestResultResponse
from local_api_tester.services import (
    execute_selected_apis,
    export_test_results,
    load_api_definitions,
    query_test_log_list,
    query_test_run_list,
)
from local_api_tester.settings import settings


class TestWorker(QThread):
    """后台线程执行 API 测试"""

    finished = Signal(list)
    error = Signal(str)

    def __init__(self, request: ApiTestRequest):
        super().__init__()
        self.request = request

    def run(self):
        from local_api_tester.exceptions import ServiceException

        try:
            results = execute_selected_apis(self.request)
            self.finished.emit(results)
        except ServiceException as e:
            self.error.emit(e.message)
        except Exception as e:
            self.error.emit(f"未知异常: {e}")


class MainWindow(QMainWindow):
    """主窗口"""

    def __init__(self):
        super().__init__()
        self.setWindowTitle("本地 API 检测工具")
        self.setMinimumSize(960, 640)
        self.resize(1100, 720)

        self._api_defs: list[ApiDefinitionResponse] = []
        self._checkboxes: list[QCheckBox] = []
        self._current_results: list[ApiTestResultResponse] = []
        self._worker: TestWorker | None = None

        self._init_ui()
        self._load_api_list()

    # ========== 界面初始化 ==========

    def _init_ui(self):
        central = QWidget()
        self.setCentralWidget(central)
        root_layout = QVBoxLayout(central)
        root_layout.setContentsMargins(10, 10, 10, 10)

        root_layout.addLayout(self._build_config_area())

        splitter = QSplitter(Qt.Horizontal)
        splitter.addWidget(self._build_api_selector())
        splitter.addWidget(self._build_result_area())
        splitter.setStretchFactor(0, 1)
        splitter.setStretchFactor(1, 3)
        root_layout.addWidget(splitter, stretch=1)

        root_layout.addLayout(self._build_buttons())
        root_layout.addWidget(self._build_detail_area())

    def _build_config_area(self) -> QGridLayout:
        layout = QGridLayout()
        layout.setHorizontalSpacing(10)

        layout.addWidget(QLabel("后端地址"), 0, 0)
        self._base_url_input = QLineEdit(settings.base_url)
        self._base_url_input.setPlaceholderText("http://localhost:8081")
        layout.addWidget(self._base_url_input, 0, 1, 1, 3)

        layout.addWidget(QLabel("用户名"), 1, 0)
        self._username_input = QLineEdit(settings.default_username)
        self._username_input.setPlaceholderText("admin")
        layout.addWidget(self._username_input, 1, 1)

        layout.addWidget(QLabel("密码"), 1, 2)
        self._password_input = QLineEdit(settings.default_password)
        self._password_input.setEchoMode(QLineEdit.Password)
        self._password_input.setPlaceholderText("请输入密码")
        layout.addWidget(self._password_input, 1, 3)

        return layout

    def _build_api_selector(self) -> QWidget:
        group = QGroupBox("API 选择")
        layout = QVBoxLayout(group)

        self._api_list_container = QVBoxLayout()
        layout.addLayout(self._api_list_container)
        layout.addStretch()

        btn_row = QHBoxLayout()
        select_all_btn = QPushButton("全选")
        select_all_btn.clicked.connect(self._on_select_all)
        invert_btn = QPushButton("反选")
        invert_btn.clicked.connect(self._on_invert_selection)
        btn_row.addWidget(select_all_btn)
        btn_row.addWidget(invert_btn)
        layout.addLayout(btn_row)

        return group

    def _build_result_area(self) -> QWidget:
        group = QGroupBox("本次测试结果")
        layout = QVBoxLayout(group)

        self._result_table = QTableWidget(0, 8)
        self._result_table.setHorizontalHeaderLabels(
            [
                "接口名称",
                "请求方法",
                "状态",
                "HTTP",
                "后端消息",
                "耗时(ms)",
                "设备摘要",
                "错误原因",
            ]
        )
        header = self._result_table.horizontalHeader()
        header.setSectionResizeMode(0, QHeaderView.ResizeMode.Stretch)
        for col in range(1, 8):
            header.setSectionResizeMode(col, QHeaderView.ResizeMode.ResizeToContents)
        self._result_table.setSelectionBehavior(
            QTableWidget.SelectionBehavior.SelectRows
        )
        self._result_table.setSelectionMode(QTableWidget.SelectionMode.SingleSelection)
        self._result_table.setEditTriggers(QTableWidget.EditTrigger.NoEditTriggers)
        self._result_table.itemSelectionChanged.connect(self._on_result_row_selected)

        layout.addWidget(self._result_table)
        return group

    def _build_buttons(self) -> QHBoxLayout:
        layout = QHBoxLayout()

        self._test_btn = QPushButton("一键测试")
        self._test_btn.clicked.connect(self._on_start_test)

        clear_btn = QPushButton("清空结果")
        clear_btn.clicked.connect(self._on_clear_results)

        history_btn = QPushButton("查看历史")
        history_btn.clicked.connect(self._on_view_history)

        export_btn = QPushButton("导出本次结果")
        export_btn.clicked.connect(self._on_export_results)

        layout.addWidget(self._test_btn)
        layout.addWidget(clear_btn)
        layout.addWidget(history_btn)
        layout.addWidget(export_btn)
        layout.addStretch()

        return layout

    def _build_detail_area(self) -> QTextEdit:
        self._detail_view = QTextEdit()
        self._detail_view.setReadOnly(True)
        self._detail_view.setMaximumHeight(180)
        self._detail_view.setPlaceholderText("点击结果表格中的某一行查看响应详情")
        return self._detail_view

    # ========== API 列表加载 ==========

    def _load_api_list(self):
        self._api_defs = load_api_definitions()
        self._checkboxes.clear()

        # 清除旧控件
        while item := self._api_list_container.takeAt(0):
            if item.widget():
                item.widget().deleteLater()

        current_category = ""
        for api_def in self._api_defs:
            if api_def.category != current_category:
                current_category = api_def.category
                cat_label = QLabel(f"── {current_category} ──")
                cat_label.setStyleSheet(
                    "color: gray; font-weight: bold; margin-top: 6px;"
                )
                self._api_list_container.addWidget(cat_label)

            cb = QCheckBox(f"{api_def.name}  ({api_def.method} {api_def.path})")
            cb.setProperty("api_key", api_def.key)
            self._checkboxes.append(cb)
            self._api_list_container.addWidget(cb)

    # ========== 事件处理 ==========

    def _on_select_all(self):
        for cb in self._checkboxes:
            cb.setChecked(True)

    def _on_invert_selection(self):
        for cb in self._checkboxes:
            cb.setChecked(not cb.isChecked())

    def _on_start_test(self):
        selected_keys = [
            cb.property("api_key") for cb in self._checkboxes if cb.isChecked()
        ]
        if not selected_keys:
            QMessageBox.warning(self, "提示", "请至少勾选一个接口")
            return

        base_url = self._base_url_input.text().strip()
        if not base_url:
            QMessageBox.warning(self, "提示", "请填写后端地址")
            return

        needs_auth = any(
            d.auth_required for d in self._api_defs if d.key in selected_keys
        )
        if needs_auth or "login" in selected_keys:
            username = self._username_input.text().strip()
            password = self._password_input.text().strip()
            if not username or not password:
                QMessageBox.warning(
                    self, "提示", "选中的接口需要登录，请填写用户名和密码"
                )
                return
        else:
            username = ""
            password = ""

        request = ApiTestRequest(
            base_url=base_url,
            username=username,
            password=password,
            selected_api_keys=selected_keys,
        )

        self._test_btn.setEnabled(False)
        self._test_btn.setText("测试中...")
        self._current_results.clear()
        self._detail_view.clear()

        self._worker = TestWorker(request)
        self._worker.finished.connect(self._on_test_finished)
        self._worker.error.connect(self._on_test_error)
        self._worker.start()

    def _on_test_finished(self, results: list):
        self._current_results = results
        self._fill_result_table(results)
        self._restore_test_btn()

    def _on_test_error(self, message: str):
        self._restore_test_btn()
        QMessageBox.critical(self, "测试失败", message)

    def _restore_test_btn(self):
        self._test_btn.setEnabled(True)
        self._test_btn.setText("一键测试")

    def _on_clear_results(self):
        self._result_table.setRowCount(0)
        self._current_results.clear()
        self._detail_view.clear()

    def _on_view_history(self):
        dialog = HistoryDialog(self)
        dialog.exec()

    def _on_export_results(self):
        if not self._current_results:
            QMessageBox.warning(self, "提示", "没有可导出的测试结果")
            return

        file_path, _ = QFileDialog.getSaveFileName(
            self, "导出测试结果", "api_test_results.json", "JSON 文件 (*.json)"
        )
        if not file_path:
            return

        try:
            export_test_results(self._current_results, file_path)
            QMessageBox.information(self, "导出成功", f"已导出到:\n{file_path}")
        except Exception as e:
            QMessageBox.critical(self, "导出失败", str(e))

    def _on_result_row_selected(self):
        rows = self._result_table.selectionModel().selectedRows()
        if not rows:
            return
        row = rows[0].row()
        if row < 0 or row >= len(self._current_results):
            return

        result = self._current_results[row]
        parts = []
        parts.append(f"<b>接口:</b> {result.api_name} ({result.api_key})")
        parts.append(f"<b>请求 URL:</b> {result.url}")
        parts.append(f"<b>请求方法:</b> {result.method}")
        parts.append(f"<b>HTTP 状态码:</b> {result.http_status or '-'}")
        parts.append(f"<b>后端响应码:</b> {result.response_code or '-'}")
        parts.append(f"<b>后端消息:</b> {result.response_message or '-'}")
        parts.append(f"<b>请求耗时:</b> {result.cost_ms} ms")

        if result.summary:
            s = result.summary
            parts.append(
                f"<b>设备摘要:</b> 总数: {s.total}, 在线: {s.online}, 离线: {s.offline}, 异常: {s.abnormal}"
            )

        if result.error_message:
            parts.append(f"<b style='color:red;'>错误:</b> {result.error_message}")

        if result.request_body:
            parts.append(
                f"<b>请求体:</b><pre>{_format_json(result.request_body)}</pre>"
            )

        if result.response_body:
            parts.append(
                f"<b>响应 JSON:</b><pre>{_format_json(result.response_body)}</pre>"
            )

        self._detail_view.setHtml("<br>".join(parts))

    # ========== 结果表格填充 ==========

    def _fill_result_table(self, results: list[ApiTestResultResponse]):
        self._result_table.setRowCount(len(results))
        for row, r in enumerate(results):
            self._result_table.setItem(row, 0, _item(r.api_name))
            self._result_table.setItem(row, 1, _item(r.method))
            self._result_table.setItem(row, 2, _status_item(r.success))
            self._result_table.setItem(
                row, 3, _item(str(r.http_status) if r.http_status else "-")
            )
            self._result_table.setItem(row, 4, _item(r.response_message or "-"))
            self._result_table.setItem(row, 5, _item(str(r.cost_ms)))
            self._result_table.setItem(row, 6, _item(_format_summary(r.summary)))
            self._result_table.setItem(row, 7, _item(r.error_message or "-"))


# ========== 辅助函数 ==========


def _item(text: str) -> QTableWidgetItem:
    item = QTableWidgetItem(text)
    item.setTextAlignment(Qt.AlignmentFlag.AlignCenter)
    return item


def _status_item(success: bool) -> QTableWidgetItem:
    text = "成功" if success else "失败"
    item = QTableWidgetItem(text)
    item.setTextAlignment(Qt.AlignmentFlag.AlignCenter)
    if success:
        item.setForeground(Qt.GlobalColor.darkGreen)
    else:
        item.setForeground(Qt.GlobalColor.red)
    return item


def _format_summary(summary) -> str:
    if summary is None:
        return "-"
    return f"总数: {summary.total}, 在线: {summary.online}, 离线: {summary.offline}, 异常: {summary.abnormal}"


def _format_json(text: str) -> str:
    try:
        parsed = json.loads(text)
        return json.dumps(parsed, ensure_ascii=False, indent=2)
    except (json.JSONDecodeError, TypeError):
        return text


class HistoryDialog(QDialog):
    """历史测试记录查看对话框"""

    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle("历史测试记录")
        self.setMinimumSize(900, 600)
        self.resize(1000, 680)
        self._log_results: list[ApiTestResultResponse] = []
        self._init_ui()
        self._load_runs()

    def _init_ui(self):
        layout = QVBoxLayout(self)

        # 上半部分：批次列表
        run_group = QGroupBox("测试批次")
        run_layout = QVBoxLayout(run_group)

        self._run_table = QTableWidget(0, 7)
        self._run_table.setHorizontalHeaderLabels(
            ["批次ID", "开始时间", "后端地址", "选中数", "成功", "失败", "耗时(ms)"]
        )
        run_header = self._run_table.horizontalHeader()
        run_header.setSectionResizeMode(0, QHeaderView.ResizeMode.ResizeToContents)
        run_header.setSectionResizeMode(1, QHeaderView.ResizeMode.ResizeToContents)
        run_header.setSectionResizeMode(2, QHeaderView.ResizeMode.Stretch)
        for col in range(3, 7):
            run_header.setSectionResizeMode(
                col, QHeaderView.ResizeMode.ResizeToContents
            )
        self._run_table.setSelectionBehavior(QTableWidget.SelectionBehavior.SelectRows)
        self._run_table.setSelectionMode(QTableWidget.SelectionMode.SingleSelection)
        self._run_table.setEditTriggers(QTableWidget.EditTrigger.NoEditTriggers)
        self._run_table.itemSelectionChanged.connect(self._on_run_selected)

        run_layout.addWidget(self._run_table)
        layout.addWidget(run_group, stretch=2)

        # 下半部分：接口明细
        log_group = QGroupBox("接口测试明细")
        log_layout = QVBoxLayout(log_group)

        self._log_table = QTableWidget(0, 8)
        self._log_table.setHorizontalHeaderLabels(
            [
                "接口名称",
                "请求方法",
                "状态",
                "HTTP",
                "后端消息",
                "耗时(ms)",
                "设备摘要",
                "错误原因",
            ]
        )
        log_header = self._log_table.horizontalHeader()
        log_header.setSectionResizeMode(0, QHeaderView.ResizeMode.Stretch)
        for col in range(1, 8):
            log_header.setSectionResizeMode(
                col, QHeaderView.ResizeMode.ResizeToContents
            )
        self._log_table.setSelectionBehavior(QTableWidget.SelectionBehavior.SelectRows)
        self._log_table.setSelectionMode(QTableWidget.SelectionMode.SingleSelection)
        self._log_table.setEditTriggers(QTableWidget.EditTrigger.NoEditTriggers)
        self._log_table.itemSelectionChanged.connect(self._on_log_selected)

        log_layout.addWidget(self._log_table)
        layout.addWidget(log_group, stretch=3)

        # 响应详情
        self._detail_view = QTextEdit()
        self._detail_view.setReadOnly(True)
        self._detail_view.setMaximumHeight(150)
        self._detail_view.setPlaceholderText("点击接口明细中的某一行查看响应详情")
        layout.addWidget(self._detail_view)

        # 关闭按钮
        btn_layout = QHBoxLayout()
        close_btn = QPushButton("关闭")
        close_btn.clicked.connect(self.accept)
        btn_layout.addStretch()
        btn_layout.addWidget(close_btn)
        layout.addLayout(btn_layout)

    def _load_runs(self):
        """加载批次列表"""
        try:
            runs = query_test_run_list()
        except Exception as e:
            QMessageBox.critical(self, "查询失败", str(e))
            return

        self._run_table.setRowCount(len(runs))
        for row, r in enumerate(runs):
            self._run_table.setItem(row, 0, _item(str(r.id)))
            self._run_table.setItem(row, 1, _item(r.started_at))
            self._run_table.setItem(row, 2, _item(r.base_url))
            self._run_table.setItem(row, 3, _item(str(r.selected_count)))
            self._run_table.setItem(row, 4, _item(str(r.success_count)))
            self._run_table.setItem(row, 5, _item(str(r.fail_count)))
            self._run_table.setItem(row, 6, _item(str(r.total_cost_ms)))

    def _on_run_selected(self):
        """选中批次后加载接口明细"""
        rows = self._run_table.selectionModel().selectedRows()
        if not rows:
            return
        row = rows[0].row()
        run_id_item = self._run_table.item(row, 0)
        if not run_id_item:
            return
        run_id = int(run_id_item.text())

        try:
            self._log_results = query_test_log_list(run_id)
        except Exception as e:
            QMessageBox.critical(self, "查询失败", str(e))
            return

        self._log_table.setRowCount(len(self._log_results))
        for i, r in enumerate(self._log_results):
            self._log_table.setItem(i, 0, _item(r.api_name))
            self._log_table.setItem(i, 1, _item(r.method))
            self._log_table.setItem(i, 2, _status_item(r.success))
            self._log_table.setItem(
                i, 3, _item(str(r.http_status) if r.http_status else "-")
            )
            self._log_table.setItem(i, 4, _item(r.response_message or "-"))
            self._log_table.setItem(i, 5, _item(str(r.cost_ms)))
            self._log_table.setItem(i, 6, _item(_format_summary(r.summary)))
            self._log_table.setItem(i, 7, _item(r.error_message or "-"))
        self._detail_view.clear()

    def _on_log_selected(self):
        """选中接口明细后展示响应详情"""
        rows = self._log_table.selectionModel().selectedRows()
        if not rows:
            return
        row = rows[0].row()
        if row < 0 or row >= len(self._log_results):
            return

        result = self._log_results[row]
        parts = []
        parts.append(f"<b>接口:</b> {result.api_name} ({result.api_key})")
        parts.append(f"<b>请求 URL:</b> {result.url}")
        parts.append(f"<b>请求方法:</b> {result.method}")
        parts.append(f"<b>HTTP 状态码:</b> {result.http_status or '-'}")
        parts.append(f"<b>后端响应码:</b> {result.response_code or '-'}")
        parts.append(f"<b>后端消息:</b> {result.response_message or '-'}")
        parts.append(f"<b>请求耗时:</b> {result.cost_ms} ms")

        if result.summary:
            s = result.summary
            parts.append(
                f"<b>设备摘要:</b> 总数: {s.total}, 在线: {s.online}, 离线: {s.offline}, 异常: {s.abnormal}"
            )

        if result.error_message:
            parts.append(f"<b style='color:red;'>错误:</b> {result.error_message}")

        if result.response_body:
            parts.append(
                f"<b>响应 JSON:</b><pre>{_format_json(result.response_body)}</pre>"
            )

        self._detail_view.setHtml("<br>".join(parts))
