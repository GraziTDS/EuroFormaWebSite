# syntax=docker/dockerfile:1

# ===== 1) Build do frontend Angular =====
FROM node:22-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ .
RUN npm run build -- --configuration production

# ===== 2) Build do backend Spring Boot (com o Angular já compilado embutido) =====
FROM eclipse-temurin:21-jdk-alpine AS backend-build
WORKDIR /app/backend
COPY backend/.mvn/ .mvn/
COPY backend/mvnw backend/pom.xml ./
COPY backend/src/ src/
COPY --from=frontend-build /app/frontend/dist/euroforma-frontend/browser/ src/main/resources/static/
RUN ./mvnw -q -DskipTests package

# ===== 3) Imagem final de runtime =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
