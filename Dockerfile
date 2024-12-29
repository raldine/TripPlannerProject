FROM eclipse-temurin:22-jdk-noble AS builder

WORKDIR /app


COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
COPY src src

RUN ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:22-jre-noble

WORKDIR /app

COPY formattedData.json .



COPY --from=builder \ 
    /app/target/TripPlannerProject-0.0.1-SNAPSHOT.jar app.jar

ENV BUS_STOP_PATH=/app/formattedData.json

# ENV SERVER_PORT=8080
# EXPOSE ${SERVER_PORT}

ENV PORT=8080
EXPOSE ${PORT}


ENTRYPOINT java -jar app.jar