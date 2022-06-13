FROM openjdk:11
ADD emp-service/target/emp-service.jar emp-service.jar
ENTRYPOINT ["java", "-jar", "emp-service.jar"]

# The line below is in case we want to start emp-service with debug mode on port 8787
# ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787", "-jar", "emp-service.jar"]