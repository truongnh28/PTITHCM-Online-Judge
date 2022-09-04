FROM openjdk:18
EXPOSE 8080
ADD target/demo-0.0.1-SNAPSHOT.jar oj.jar
ENTRYPOINT ["java", "-jar", "/oj.jar"]