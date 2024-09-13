FROM amazoncorretto:21-alpine-jdk
COPY target/*.jar stats.jar
ENTRYPOINT ["java","-jar","/stats.jar"]