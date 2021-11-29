#build stage
FROM maven:3.8.3-openjdk-11 AS BUILD

COPY src /home/app/src
COPY pom.xml /home/app/pom.xml

RUN mvn -f /home/app/pom.xml clean package

#package stage
FROM openjdk:11-jre-slim
COPY --from=BUILD /home/app/target/battleship-0.0.1-SNAPSHOT.jar /usr/local/lib/battleship.jar

ENV PROFILE="default"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/battleship.jar", "--spring.profiles.active=${PROFILE}"]