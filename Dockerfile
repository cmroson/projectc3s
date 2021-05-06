FROM openjdk:8-jre-alpine
#Port Jenkins APP is using
EXPOSE 8080
# ADD application to container(just the executable JAR)
ADD target/helloWorld-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT for the the application
ENTRYPOINT ["java","-jar","app.jar"]

