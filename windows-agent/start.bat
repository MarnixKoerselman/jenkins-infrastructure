@REM https://learn.microsoft.com/en-us/java/openjdk/download-major-urls

if "%JENKINS_CONTROLLER_IP%" neq "" echo %JENKINS_CONTROLLER_IP% jenkins.test.local >> C:\Windows\System32\drivers\etc\hosts
curl -O http://jenkins.test.local/swarm/swarm-client.jar || exit /b
java -jar swarm-client.jar -executors 1 -url http://jenkins.test.local -username swarm-user -password swarm-password -executors 1 -labels "windows x64 docker"
