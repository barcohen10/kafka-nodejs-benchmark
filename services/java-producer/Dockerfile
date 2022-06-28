FROM openjdk:13-alpine AS base
WORKDIR /service

FROM base AS build
COPY . ./

RUN ./gradlew clean --info --no-daemon
RUN ./gradlew build --info --no-daemon

FROM base AS release
WORKDIR /service

COPY --from=build /service/build/libs/spring-boot-1.0.0.jar /service/application.jar
COPY --from=build /service/configuration/prod.properties /service/configuration/prod.properties

ENTRYPOINT ["java", "-jar","/service/application.jar"]