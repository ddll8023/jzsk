@echo off
chcp 65001 >nul
echo 正在启动 szy 应用...
java -jar "%~dp0backend\szy-new\target\szy-new-0.0.1-SNAPSHOT.jar"
pause