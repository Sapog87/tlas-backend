FROM maven:3.8.5-openjdk-17 AS dependencies

WORKDIR /opt/app
COPY api/pom.xml api/pom.xml
COPY service/pom.xml service/pom.xml
COPY pom.xml .

RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:go-offline

FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /opt/app
COPY --from=DEPENDENCIES /root/.m2 /root/.m2
COPY --from=DEPENDENCIES /opt/app/ /opt/app
COPY api/src /opt/app/api/src
COPY service/src /opt/app/service/src

RUN mvn -B -e clean install -DskipTests

FROM openjdk:17-slim

WORKDIR /opt/app
COPY --from=BUILDER /opt/app/service/target/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]