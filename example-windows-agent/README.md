# Windows Agents

## Requirements

- Windows 10/11... or server
- Mixing Linux and Windows Docker containers on the same host is a headache, so save yourself the headache and make a VM with Windows Server; edit the hosts file if applicable, so that VM can resolve `jenkins.test.local` (or `jenkins.mshome.net`)
- Install docker (<https://learn.microsoft.com/en-us/virtualization/windowscontainers/quick-start/set-up-environment?tabs=dockerce#windows-server-2>)
- Install JRE/JDK 21 (see [Dockerfile](Dockerfile) for an example implementation)
- Create an empty directory (on the VM), copy [ws2022-swarm-agent.bat](ws2022-swarm-agent.bat) over to that directory, and start it
- If you want to get fancy, [run the agent as a Windows Service](..\windows-agent-service\README.md)

Alternatively, using docker + compose:

- docker compose on Windows Server: see <https://github.com/docker/compose/releases>

  ```powershell
  Start-BitsTransfer -Source https://github.com/docker/compose/releases/download/v2.17.2/docker-compose-windows-x86_64.exe -Destination $Env:ProgramFiles\Docker\cli-plugins\docker-compose.exe
  ```

- in directory windows-agent, run `docker compose up`
- NB: this results in an agent that does not have 'docker' capabilities

## Notes

Install OpenSSH server on Windows Server Core, to enable VSCode remote connections: <https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_install_firstuse?tabs=powershell>

## Get Java

Manually download from one of these locations:

- <https://adoptium.net/temurin/archive/?version=11> or <https://adoptium.net/temurin/releases/>
- <https://learn.microsoft.com/en-us/java/openjdk/download-major-urls>

Scripted download example:

```powershell
Start-BitsTransfer -Source "https://aka.ms/download-jdk/microsoft-jdk-21-windows-x64.msi" -Destination java.msi
```

Install myCA.cer in the keystore:

```powershell
Set-Location "C:\Program Files\Microsoft\jdk-21.0.9.10-hotspot\lib\security"
keytool -import -trustcacerts -keystore cacerts -storepass changeit -noprompt -alias myCA -file C:\jenkins\myCA.cer
```
