FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE="/target/v8logscannerweb-0.1.jar"
ADD ${JAR_FILE} app.jar
RUN echo "Oh dang look at that $JAR_FILE"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]