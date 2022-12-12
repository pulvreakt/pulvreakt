# syntax=docker/dockerfile:1

#FROM gradle:jdk17 as build
#RUN apt-get update && apt-get install git && git clone https://github.com/nicolasfara/pulverization-framework.git
#WORKDIR pulverization-framework
#RUN git checkout feat/new-dsl
#RUN ./gradlew examples:example-03:generateJars

FROM eclipse-temurin:17-jre
WORKDIR /root/
RUN apt-get update && apt-get install -y netcat
COPY .docker/entrypoint.sh ./
COPY build/libs/*.jar ./
RUN chmod +x entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
