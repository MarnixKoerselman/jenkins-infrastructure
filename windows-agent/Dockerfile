#escape=`
FROM mcr.microsoft.com/windows/servercore:ltsc2019
ADD java.msi C:\TEMP\
RUN msiexec /quiet /i C:\TEMP\java.msi
ADD start.bat start.bat
CMD [ "start.bat" ]
