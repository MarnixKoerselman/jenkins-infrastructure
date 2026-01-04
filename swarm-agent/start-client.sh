#!/bin/bash

# exit when ctrl-c is used (during debugging/monitoring)
trap " echo INT received ; exit" INT
trap " echo SIGTERM received ; exit " SIGTERM
trap " echo SIGINT received ; exit " SIGINT

JENKINS_SWARM_USER=$(cat /run/secrets/JENKINS_SWARM_USER)
JENKINS_SWARM_PASSWORD=$(cat /run/secrets/JENKINS_SWARM_PASSWORD)

while true
do
wget $JENKINS_URL/swarm/swarm-client.jar -O swarm-client.jar
# see https://plugins.jenkins.io/swarm/
java -jar swarm-client.jar \
  -description "based on ubuntu:24.04" \
  -executors 1 \
  -fsroot $PWD \
  -labels "docker-engine linux ubuntu-24.04" \
  -mode exclusive \
  -password $JENKINS_SWARM_PASSWORD \
  -retryBackOffStrategy exponential \
  -url $JENKINS_URL \
  -username $JENKINS_SWARM_USER \
  -webSocket
echo "sleep and retry, swarm-client exit code: $?"
sleep 10
done
