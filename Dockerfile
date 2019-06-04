FROM java:8
COPY target/project-service-0.0.1-SNAPSHOT.jar /tmp/project-service-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/tmp/project-service-0.0.1-SNAPSHOT.jar", "--server.servlet.context-path=/project", "--spring.profiles.active=dev" ,"&"]
