FROM openjdk:8-jre-alpine

MAINTAINER "Tom Paulus" <tom@whitestar.systems>

ENV APP_NAME=ms-mon-agent \
    CONFIG_PATH=/var/lib/$APP_NAME/config.properties

WORKDIR /$APP_NAME

COPY agent.jar agent.jar

CMD java -jar agent.jar
VOLUME ["$CONFIG_PATH"]
