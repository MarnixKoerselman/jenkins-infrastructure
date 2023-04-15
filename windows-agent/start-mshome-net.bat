if "%JENKINS_CONTROLLER_IP%" neq "" echo %JENKINS_CONTROLLER_IP% jenkins.mshome.net >> C:\Windows\System32\drivers\etc\hosts
curl -O http://jenkins.mshome.net/swarm/swarm-client.jar || exit /b
java -jar swarm-client.jar -executors 1 -url http://jenkins.mshome.net -username swarm-user -password swarm-password -executors 1 -labels "windows x64 docker"
