@REM the secret must be changed for every Jenkins instance, but here is an example
set INBOUND_AGENT_SECRET=1fee0c62ab31e52dd9c610c67eda94ab5098ba28b4484e0a5d68d35430bd1b40
docker run --rm jenkins/inbound-agent:windowsservercore-ltsc2022 -name "ws2022-inbound-agent" -secret %INBOUND_AGENT_SECRET% -url https://jenkins.mshome.net -webSocket -workDir "%cd%"
