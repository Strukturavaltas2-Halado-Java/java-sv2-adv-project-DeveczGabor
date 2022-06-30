FROM openjdk:17
WORKDIR /opt/app
COPY target/*.jar petshops.jar
CMD ["java", "-jar", "petshops.jar"]