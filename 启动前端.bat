@echo off
chcp 65001 >nul
echo ==============================
echo   启动前端服务 (frontendV2)
echo ==============================

cd /d D:\demo\java\jzsk\frontendV2

echo 正在启动 Vite 开发服务器 ...
call npm run dev

pause
