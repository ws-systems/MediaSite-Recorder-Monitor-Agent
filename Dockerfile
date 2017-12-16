FROM java:8

MAINTAINER "Tom Paulus" <tom@whitestar.systems>

ENV APP_NAME=ms-mon-agent \
    CONFIG_PATH=/var/lib/$APP_NAME/config.properties

WORKDIR /$APP_NAME
COPY gradle gradle
COPY src src
COPY *.gradle gradlew ./

RUN ./gradlew clean build fatJar && \
    cp build/libs/mediasite-monitor-agent-1.0.jar agent.jar && \
    ./gradlew clean && \
    rm -rf .gradle *.gradle gradle* src /root/.gradle

CMD java -jar agent.jar
VOLUME ["$CONFIG_PATH"]
