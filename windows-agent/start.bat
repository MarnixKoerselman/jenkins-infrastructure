curl -O http://jenkins.test.local/swarm/swarm-client.jar
java -jar swarm-client.jar -executors 1 -url http://jenkins.test.local -username swarm-user -password swarm-password -executors 1 -labels "windows docker"
