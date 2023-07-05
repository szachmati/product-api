Spring Boot Product API
=====================

### Build application locally
1. Run `./mvnw clean package` or `mvn clean package` if you have locally installed Maven
2. Build local Postgres DB instance `docker compose -f docker-compose-local.yml up --build -d`
3. Run `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` when Postgres will be available
4. Spring Boot will listen of default `8080` port and Postgres on `4000`
5. Swagger available at `http://localhost:8080/swagger-ui/index.html`

### Build application with Docker Compose
1. Run `./mvnw clean package` or `mvn clean package` if you have locally installed Maven
2. Run `docker compose -f docker-compose.yml up --build -d`
3. Spring Boot will listen on `8010` port and Postgres DB on `5432`
4. Swagger available at `http://localhost:8010/swagger-ui/index.html`

### Build native image
`mvn -Pnative spring-boot:build-image`
