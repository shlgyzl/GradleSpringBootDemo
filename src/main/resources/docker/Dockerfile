FROM java:8
VOLUME /tmp
ADD admin-0.0.1-SNAPSHOT.jar /admin.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/admin.jar"]
