FROM eclipse-temurin:18-jre
MAINTAINER "Nicolas Farabegoli"
COPY build/libs/sensors-all.jar /home/sensors-all.jar
COPY .docker/wait-rabbit.sh /home/wait-rabbit.sh

RUN apt-get update
RUN apt-get install -y netcat

RUN chmod +x /home/wait-rabbit.sh

ENTRYPOINT [ "/home/wait-rabbit.sh", "sensors-all.jar" ]
