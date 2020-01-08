
FROM openjdk:latest
COPY ./target/twitter-1.0-SNAPSHOT.jar twitter-1.0-SNAPSHOT.jar
CMD ["java","-jar","twitter-1.0-SNAPSHOT.jar"]

