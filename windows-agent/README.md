# Windows Agents

## Requirements

- Windows 10 or server
- Mixing Linux and Windows Docker containers on the same host is a headache, so save yourself and make a VM with Windows Server (edit the hosts file if applicable, so that machine 'knows' jenkins.test.local)
- Install docker
- Install JRE/JDK 11
- Create an empty directory (on the VM), copy [start.bat](start.bat) over to that directory, and start it
- If you want to get fancy, install the agents as a Windows Service - see <https://github.com/winsw/winsw> and <https://github.com/winsw/winsw/blob/master/doc/xmlConfigFile.md>
