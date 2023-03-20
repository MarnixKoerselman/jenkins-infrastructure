FROM ubuntu:22.04
RUN apt-get update && apt-get install -y wget openjdk-11-jre-headless curl && apt-get clean
ADD start-client.sh /
CMD [ "/start-client.sh" ]
