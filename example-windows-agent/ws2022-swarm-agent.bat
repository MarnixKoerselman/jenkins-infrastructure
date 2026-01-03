@echo off
setlocal

if "%JENKINS_URL%" equ "" (
    set JENKINS_URL=https://%JENKINS_CONTROLLER_NAME%
)
if "%JENKINS_URL%" equ "" (
    @echo The Jenkins controller must be specified as argument, or by setting environment variable JENKINS_CONTROLLER_NAME
    exit /b 1
)

curl -O %JENKINS_URL%/swarm/swarm-client.jar || exit /b
java -version
java -jar swarm-client.jar -executors 1 -url %JENKINS_URL% -name ws2022-swarm-agent -username swarm-user -password swarm-password -labels "docker-engine windows ws2022 x64" -fsroot %cd%
