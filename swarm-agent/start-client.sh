#!/bin/bash

# exit when ctrl-c is used (during debugging/monitoring)
trap " echo INT received ; exit" INT
trap " echo SIGTERM received ; exit " SIGTERM
trap " echo SIGINT received ; exit " SIGINT

JENKINS_SWARM_USER=`cat /run/secrets/JENKINS_SWARM_USER`
JENKINS_SWARM_PASSWORD=`cat /run/secrets/JENKINS_SWARM_PASSWORD`

while true
do
wget $JENKINS_URL/swarm/swarm-client.jar -O /swarm-client.jar
java -jar /swarm-client.jar -executors 1 -url $JENKINS_URL -username $JENKINS_SWARM_USER -password $JENKINS_SWARM_PASSWORD -deleteExistingClients -disableClientsUniqueId -executors 1 -labels "linux ubuntu-20.04"
echo "sleep and retry, swarm-client exit code: $?"
sleep 10
done
