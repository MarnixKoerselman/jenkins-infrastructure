
# allow simple (or no) password
secedit.exe /export /cfg secconfig.cfg
notepad secconfig.cfg
secedit.exe /configure /db %windir%\securitynew.sdb /cfg secconfig.cfg /areas SECURITYPOLICY

# change the password
net user administrator *

# remove Windows Defender
Uninstall-WindowsFeature -Name Windows-Defender -Verbose

# if the worker VM is on a network configuration with static IP addresses (e.g. DEVOPSNAT), then set the VM's IP address:
New-NetIPAddress -IPAddress 192.168.197.2 -DefaultGateway 192.168.197.1 -AddressFamily IPv4 -PrefixLength 24 -InterfaceAlias 'Ethernet'
Set-DnsClientServerAddress -InterfaceAlias 'Ethernet' -ServerAddresses 192.168.197.1,8.8.8.8

# install docker on Windows Server
Install-Module -Name DockerMsftProvider -Repository PSGallery -Force -Verbose
Install-Package -Name Docker -ProviderName DockerMsftProvider -Force -Verbose
Invoke-WebRequest "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-Windows-x86_64.exe" -UseBasicParsing -OutFile $Env:ProgramFiles\docker\docker-compose.exe
Restart-Computer

# on the host for the VM, prepare the VM for nested virtualization:
# https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/user-guide/nested-virtualization
# use: worker-vm-create.ps1
Install-WindowsFeature -Name Hyper-V -Verbose

# install the Jenkins swarm client
# https://wiki.jenkins.io/display/JENKINS/Swarm+Plugin
# https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/
make a docker image with this functionality?
New-Item -Path $Env:SystemDrive\ -Name jenkins -ItemType Directory
Invoke-WebRequest https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/3.9/swarm-client-3.9.jar -UseBasicParsing -OutFile $Env:SystemDrive\jenkins\swarm-client.jar 
