# Windows Agents

## Requirements

- Windows 10/11... or server
- Mixing Linux and Windows Docker containers on the same host is a headache, so save yourself and make a VM with Windows Server; edit the hosts file if applicable, so that machine 'knows' jenkins.test.local (or jenkins.mshome.net)
- Install docker
- Install JRE/JDK 11
- Create an empty directory (on the VM), copy [start.bat](start.bat) over to that directory, and start it
- If you want to get fancy, [run the agent as a Windows Service](..\windows-agent-service\README.md)

Alternatively, using docker + compose:
- docker compose on Windows Server: see <https://github.com/docker/compose/releases>

  ```Start-BitsTransfer -Source https://github.com/docker/compose/releases/download/v2.17.2/docker-compose-windows-x86_64.exe -Destination $Env:ProgramFiles\Docker\cli-plugins\docker-compose.exe```

- in directory windows-agent, run ```docker compose up```

## Notes

Install OpenSSH server on Windows Server Core, to enable VSCode remote connections: <https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_install_firstuse?tabs=powershell>

## Get Java

Manually download from one of these locations:

- https://adoptium.net/temurin/archive/?version=11 or https://adoptium.net/temurin/releases/
- https://learn.microsoft.com/en-us/java/openjdk/download-major-urls

Scripted download example:
```language=powershell
Start-BitsTransfer -Source "https://aka.ms/download-jdk/microsoft-jdk-11-windows-x64.msi" -Destination java.msi
```
