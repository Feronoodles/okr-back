FROM openjdk:17.0.2-jdk

RUN mkdir /app

WORKDIR /app

COPY target/okr-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8081

CMD java -jar app.jar