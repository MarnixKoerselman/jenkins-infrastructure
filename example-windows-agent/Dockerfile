#escape=`
FROM mcr.microsoft.com/windows/servercore:ltsc2022
# ADD java.msi c:\temp\
RUN mkdir c:\temp &&`
    powershell Start-BitsTransfer -Source "https://aka.ms/download-jdk/microsoft-jdk-21-windows-x64.msi" -Destination c:\temp\java.msi &&`
    msiexec /quiet /i c:\temp\java.msi &&`
    rd /s /q c:\temp
WORKDIR c:\jenkins
ADD ws2022-swarm-agent.bat c:\jenkins\ws2022-swarm-agent.bat
CMD [ "c:\\jenkins\\ws2022-swarm-agent.bat" ]
