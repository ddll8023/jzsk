@echo off
chcp 65001 >nul
echo ==============================
echo   启动后端服务 (backendV2)
echo ==============================

set JAVA_HOME=C:\Users\17289\.jdks\corretto-1.8.0_452
set MAVEN_HOME=D:\maven
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

cd /d D:\demo\java\jzsk\backendV2

echo 正在启动 Spring Boot ...
call mvn spring-boot:run

pause
