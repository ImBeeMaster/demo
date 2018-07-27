FROM anapsix/alpine-java
WORKDIR /tmp
COPY *.jar .
EXPOSE 8080
CMD [ "java", "-jar", "./demo.jar" ]
