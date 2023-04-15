# Jenkins Swarm Agent as Windows Service

Kohsuke and Oleg have made it easy to install the agents as a Windows Service - see <https://github.com/winsw/winsw> and <https://github.com/winsw/winsw/blob/master/doc/xmlConfigFile.md>

The following snippet uses PowerShell to download (the latest, at time of writing) WinSW executable, which - combined with [jenkins-agent.xml](jenkins-agent.xml) - installs a Windows Serice that downloads the Jenkins Swarm client from the Jenkins Controller, and starts it. The configuration is fairly minimal; more options are available.

```language=powershell
Start-BitsTransfer -Source https://github.com/winsw/winsw/releases/download/v2.12.0/WinSW-x64.exe -Destination jenkins-agent.exe
.\jenkins-agent.exe install
.\jenkins-agent.exe start
```
