@echo off
echo 启动本地MySQL配置的后端服务...

set SPRING_PROFILES_ACTIVE=localmysql
set MAVEN_OPTS=-Xmx512m -Xms256m -XX:MaxMetaspaceSize=256m
mvn spring-boot:run

pause