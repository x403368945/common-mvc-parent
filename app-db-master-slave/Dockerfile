FROM openjdk:8
LABEL maintainer="Conor <x403368945@126.com>"
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]