@echo off
echo 启动本地配置的后端服务...

set SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run

pause