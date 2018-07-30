FROM anapsix/alpine-java
WORKDIR /tmp
COPY *.jar /home
EXPOSE 8080
CMD [ "java", "-jar", "/home/demo.jar" ]
