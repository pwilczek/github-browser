FROM openjdk:8-jre-alpine
COPY ./build/libs/github-browser-0.0.1-SNAPSHOT.jar /opt/github-browser.jar
CMD ["java", "-jar", "/opt/github-browser.jar"]

