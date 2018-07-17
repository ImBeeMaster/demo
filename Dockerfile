FROM anapsix/alpine-java
WORKDIR /tmp
COPY *.jar .
EXPOSE 8080
CMD [ "java", "-jar", "./demo-0.0.19-SNAPSHOT.jar" ]
