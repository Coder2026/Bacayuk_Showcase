# contoh ringkas
FROM gradle:8.14.2-jdk21-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar -x test

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/app.jar app.jar
CMD ["java","-jar","app.jar"]