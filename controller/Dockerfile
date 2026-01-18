FROM jenkins/jenkins:lts

USER root
# Keep the uncompressed war file outside jenkins_home, as this could cause confusion when moving between jenkins versions.
RUN mkdir /var/cache/jenkins
RUN chown -R jenkins:jenkins /var/cache/jenkins
ENV JENKINS_OPTS="--webroot=/var/cache/jenkins/war"
# Install docker
# RUN apt-get update && apt-get install -y lsb-release ca-certificates curl && \
#     install -m 0755 -d /etc/apt/keyrings && \
#     curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc && \
#     chmod a+r /etc/apt/keyrings/docker.asc && \
#     echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] \
#     https://download.docker.com/linux/debian $(. /etc/os-release && echo \"$VERSION_CODENAME\") stable" \
#     | tee /etc/apt/sources.list.d/docker.list > /dev/null && \
#     apt-get update && apt-get install -y docker-ce-cli && \
#     apt-get clean && rm -rf /var/lib/apt/lists/*

# RUN curl -L "https://github.com/docker/compose/releases/download/v2.16.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
#  chmod +x /usr/local/bin/docker-compose && \
#  ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# NB: won't work with permissions on docker.sock
USER jenkins

RUN jenkins-plugin-cli --plugins \
    authorize-project \
    configuration-as-code \
    credentials-binding \
    docker-workflow \
    github \
    github-branch-source \
    job-dsl \
    junit \
    markdown-formatter \
    matrix-auth \
    pipeline-graph-view \
    pipeline-stage-view \
    pipeline-utility-steps \
    ssh-slaves \
    swarm \
    warnings-ng \
    ws-cleanup \
    htmlpublisher
# blueocean git workflow-aggregator antisamy-markup-formatter
