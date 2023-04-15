#escape=`
FROM mcr.microsoft.com/windows/servercore:ltsc2019
# ADD java.msi c:\temp\
RUN mkdir c:\temp &&`
    powershell Start-BitsTransfer -Source "https://aka.ms/download-jdk/microsoft-jdk-11-windows-x64.msi" -Destination c:\temp\java.msi &&`
    msiexec /quiet /i c:\temp\java.msi &&`
    rd /s /q c:\temp
ADD start.bat start.bat
CMD [ "c:\\start.bat" ]
