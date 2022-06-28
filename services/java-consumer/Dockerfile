FROM openjdk:13-alpine AS base
WORKDIR /java-consumer

FROM base AS build
COPY . ./

RUN ./gradlew clean --info --no-daemon
RUN ./gradlew build --info --no-daemon

FROM base AS release
WORKDIR /java-consumer

COPY --from=build /java-consumer/build/libs/java-consumer-1.0.0.jar /java-consumer/application.jar
COPY --from=build /java-consumer/configuration/prod.properties /java-consumer/configuration/prod.properties

ENTRYPOINT ["java", "-jar","/java-consumer/application.jar"]