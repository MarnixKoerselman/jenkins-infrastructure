# NB: this is a Linux image, which is currently not supported (as LCOW) in WS2019

#escape=`

FROM jenkins/jenkins:latest

# RUN /usr/local/bin/install-plugins.sh git mstest matrix-auth workflow-aggregator docker-workflow blueocean credentials-binding
RUN /usr/local/bin/install-plugins.sh docker-workflow blueocean

ENV JENKINS_USER jenkins
ENV JENKINS_PASS jenkins

ARG JENKINS_URL_VALUE
ENV JENKINS_URL ${JENKINS_URL_VALUE}

# Skip initial setup
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY default-user.groovy /usr/share/jenkins/ref/init.groovy.d/

# VOLUME /var/jenkins_home
