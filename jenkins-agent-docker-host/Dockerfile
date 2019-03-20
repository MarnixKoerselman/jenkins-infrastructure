# A Windows based image with Docker engine (EE) installed, that registers itself as a Jenkins Agent.

# escape=`

# Get OpenJDK nanoserver container
FROM openjdk:8-windowsservercore as openjdk

SHELL ["powershell", "-Command", "$ErrorActionPreference = 'Stop'; $ProgressPreference = 'SilentlyContinue';"]


# install the Jenkins Swarm plugin client
# https://wiki.jenkins.io/display/JENKINS/Swarm+Plugin

# https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/3.9/swarm-client-3.9.jar
