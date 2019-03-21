FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

COPY target/sing-up-0.0.1-SNAPSHOT.jar sing-up.jar

ENTRYPOINT ["sh", "-c"]

CMD ["java -jar /app/sing-up.jar"]