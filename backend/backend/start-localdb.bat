@echo off
echo 启动本地数据库配置的后端服务...

set SPRING_PROFILES_ACTIVE=localdb
mvn spring-boot:run

pause