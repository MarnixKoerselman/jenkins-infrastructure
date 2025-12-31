@echo off
setlocal

set JENKINS=%1
if "%JENKINS%" equ "" (
    set JENKINS=%JENKINS_CONTROLLER_NAME%
)
if "%JENKINS%" equ "" (
    @echo The Jenkins controller must be specified as argument, or by setting environment variable JENKINS_CONTROLLER_NAME
    exit /b 1
)

curl -O http://%JENKINS%/swarm/swarm-client.jar || exit /b
java -jar swarm-client.jar -executors 1 -fsroot %cd% -labels "docker-engine windows x64" -name windows-swarm-agent -password swarm-password -url http://%JENKINS% -username swarm-user

curl -O http://%JENKINS%/swarm/swarm-client.jar || exit /b
java -version
java -jar swarm-client.jar -executors 1 -url http://%JENKINS% -name ws2022-swarm-agent -username swarm-user -password swarm-password -labels "docker-engine windows ws2022 x64" -fsroot %cd%
