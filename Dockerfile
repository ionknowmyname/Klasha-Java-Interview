FROM openjdk:17-alpine
VOLUME /tmp
COPY target/klasha-test-0.0.1-SNAPSHOT.jar klasha-test.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "klasha-test.jar"]