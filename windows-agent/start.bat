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
java -jar swarm-client.jar -executors 1 -url http://%JENKINS% -name windows-swarm-agent -username swarm-user -password swarm-password -executors 1 -labels "windows x64 docker" -fsroot %cd%
