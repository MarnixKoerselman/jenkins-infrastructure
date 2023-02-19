FROM jenkins/jenkins:lts

USER root
RUN apt-get -y update && \
 apt-get -y install apt-transport-https ca-certificates curl gnupg-agent software-properties-common && \
 curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add && \
 add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo \"$ID\") $(lsb_release -cs) stable" && \
 apt-get update && \
 apt-get -y install docker-ce docker-ce-cli containerd.io
RUN
# RUN curl -L "https://github.com/docker/compose/releases/download/v2.16.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
#  chmod +x /usr/local/bin/docker-compose && \
#  ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
# USER jenkins <= won't work with permissions on docker.sock

RUN jenkins-plugin-cli --plugins credentials-binding configuration-as-code job-dsl antisamy-markup-formatter swarm docker-workflow ws-cleanup
# blueocean ssh-slaves git workflow-aggregator  matrix-auth
