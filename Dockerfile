FROM openjdk:14-alpine
COPY target/service-kotlin-template-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar","app.jar"]
EXPOSE 8080