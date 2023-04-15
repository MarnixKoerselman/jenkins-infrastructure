# Windows Agents

## Requirements

- Windows 10 or server
- Mixing Linux and Windows Docker containers on the same host is a headache, so save yourself and make a VM with Windows Server (edit the hosts file if applicable, so that machine 'knows' jenkins.test.local)
- Install docker
- Install JRE/JDK 11
- Create an empty directory (on the VM), copy [start.bat](start.bat) over to that directory, and start it
- If you want to get fancy, install the agents as a Windows Service - see <https://github.com/winsw/winsw> and <https://github.com/winsw/winsw/blob/master/doc/xmlConfigFile.md>

Alternative, using docker + compose:
- docker compose on Windows Server: see <https://github.com/docker/compose/releases>

  ```Start-BitsTransfer -Source https://github.com/docker/compose/releases/download/v2.17.2/docker-compose-windows-x86_64.exe -Destination $Env:ProgramFiles\Docker\cli-plugins\docker-compose.exe```

## Notes

Install OpenSSH server on Windows Server Core, to enable VSCode remote connections: <https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_install_firstuse?tabs=powershell>
