FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="Yana Guseva"
WORKDIR /opt/app
ARG JAR_FILE=build/libs/requests-0.0.1.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","app.jar"]