#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY . /home/app/
RUN mvn -f /home/app/pom.xml -pl server -am clean package

#
# Package stage
#
FROM openjdk:8-alpine
COPY --from=build /home/app/server/target/server-*.jar /home/minigolf/server.jar
COPY tracks/ /home/minigolf/tracks/
EXPOSE 4242
WORKDIR /home/minigolf
ENTRYPOINT ["java","-jar","server.jar"]