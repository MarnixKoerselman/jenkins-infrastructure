FROM ubuntu:24.04

ARG user=jenkins
ARG JENKINS_AGENT_HOME=/home/${user}
ENV JENKINS_AGENT_HOME=${JENKINS_AGENT_HOME}
ARG AGENT_WORKDIR="${JENKINS_AGENT_HOME}/agent"
ENV AGENT_WORKDIR=${AGENT_WORKDIR}

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
    && apt-get install -y --no-install-recommends git openjdk-21-jre-headless wget \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
# Install docker
RUN apt-get update && apt-get install -y curl \
    && install -m 0755 -d /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc \
    && chmod a+r /etc/apt/keyrings/docker.asc \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo \"$VERSION_CODENAME\") stable" | tee /etc/apt/sources.list.d/docker.list \
    && apt-get update && apt-get install -y docker-ce-cli && apt-get clean && rm -rf /var/lib/apt/lists/*

# VOLUME directive must happen after setting up permissions and content
VOLUME "${AGENT_WORKDIR}" "${JENKINS_AGENT_HOME}"/.jenkins "/tmp" "/run" "/var/run"
WORKDIR "${AGENT_WORKDIR}"
COPY start-client.sh /usr/local/bin/start-client.sh
CMD ["/usr/local/bin/start-client.sh"]
