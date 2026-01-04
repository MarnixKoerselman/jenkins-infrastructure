FROM docker:dind
ARG user=jenkins
ARG JENKINS_AGENT_HOME=/home/${user}
ENV JENKINS_AGENT_HOME=${JENKINS_AGENT_HOME}
ARG AGENT_WORKDIR="${JENKINS_AGENT_HOME}/agent"
ENV AGENT_WORKDIR=${AGENT_WORKDIR}
VOLUME "${AGENT_WORKDIR}"
WORKDIR "${AGENT_WORKDIR}"
COPY --chmod=755 start-client.sh /usr/local/bin/start-client.sh
CMD ["/usr/local/bin/start-client.sh"]
