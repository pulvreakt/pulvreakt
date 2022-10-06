FROM eclipse-temurin:18-jre
MAINTAINER "Nicolas Farabegoli"
COPY build/libs/behaviour-all.jar /home/behaviour-all.jar

RUN apt-get update
RUN apt-get install -y netcat

COPY .docker/wait-rabbit.sh /home/wait-rabbit.sh
RUN chmod +x /home/wait-rabbit.sh

ENTRYPOINT [ "/home/wait-rabbit.sh", "behaviour-all.jar" ]
