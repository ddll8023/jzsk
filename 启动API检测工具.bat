@echo off
chcp 65001 >nul 2>&1
cd /d "%~dp0local-api-tester"
set PYTHONPATH=src
.venv\Scripts\python.exe -m local_api_tester.main
pause
