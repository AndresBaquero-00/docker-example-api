FROM maven:3.9.9-amazoncorretto-17-al2023 AS build
WORKDIR /app
COPY . ./
RUN mvn clean package -DskipTests

FROM maven:3.9.9-amazoncorretto-17-al2023 AS prod
WORKDIR /app
COPY --from=build /app/target/docker-example.jar ./docker-example.jar
EXPOSE 8080
CMD ["java", "-jar", "docker-example.jar"]
