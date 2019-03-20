
# allow simple (or no) password
secedit.exe /export /cfg secconfig.cfg
notepad secconfig.cfg
secedit.exe /configure /db %windir%\securitynew.sdb /cfg secconfig.cfg /areas SECURITYPOLICY

# change the password
net user administrator *

# remove Windows Defender
Uninstall-WindowsFeature -Name Windows-Defender -Verbose

# install docker on Windows Server
Install-Module -Name DockerMsftProvider -Repository PSGallery -Force -Verbose
Install-Package -Name Docker -ProviderName DockerMsftProvider -Force -Verbose
Invoke-WebRequest "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-Windows-x86_64.exe" -UseBasicParsing -OutFile $Env:ProgramFiles\docker\docker-compose.exe
Restart-Computer

# on the host for the VM, prepare the VM for nested virtualization:
# https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/user-guide/nested-virtualization
# use: worker-vm-create.ps1
Install-WindowsFeature -Name Hyper-V -Verbose