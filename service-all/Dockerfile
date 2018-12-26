FROM java:8
 
RUN mkdir /app
WORKDIR /app
COPY /target/*.jar /app/app.jar
VOLUME /tmp
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar", "--spring.profiles.active=test"]
EXPOSE 8001
