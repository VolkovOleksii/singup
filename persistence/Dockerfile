FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

COPY target/persistence-0.0.1-SNAPSHOT.jar persistence.jar

ENTRYPOINT ["sh", "-c"]

CMD ["java -jar /app/persistence.jar"]