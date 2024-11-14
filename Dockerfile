FROM openjdk:17-slim AS base
RUN apt-get update && apt-get install -y curl bash vim htop && apt-get clean

FROM base AS builder
WORKDIR /app

COPY . .
RUN /app/gradlew clean build --no-daemon

FROM base
WORKDIR /app
COPY --from=builder /app/build/libs/worksheet-backend-1.0.0.jar /app/app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]

