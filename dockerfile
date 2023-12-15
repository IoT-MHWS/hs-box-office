FROM openjdk:17-alpine

COPY "build/libs/hs-box-office-*.jar" application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
