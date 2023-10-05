#
# Build stage
#
FROM maven:3.8.5-openjdk-17-slim AS build
ENV HOME=/home/app
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn dependency:go-offline --batch-mode
RUN mvn verify --fail-never --batch-mode
COPY src /home/app/src
RUN mvn clean package --batch-mode -DskipTests

#
# Package stage
#
FROM openjdk:17-alpine
COPY --from=build /home/app/target/csdc26bb-discord-bot-1.0.0-SNAPSHOT.jar /usr/local/lib/csdc26bb-discord-bot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/csdc26bb-discord-bot.jar"]