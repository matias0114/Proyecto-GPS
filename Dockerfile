# Etapa 1: Build del proyecto (opcional si lo haces en CI)
# Puedes omitir esta etapa si ya construyes el .jar en GitHub Actions y solo necesitas empaquetarlo

# FROM maven:3.9.4-eclipse-temurin-17 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con el .jar
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el .jar ya construido. Asegúrate de que el nombre sea correcto o usa wildcard
COPY target/*.jar app.jar

# Puerto por defecto para Spring Boot
EXPOSE 8080

# Comando para iniciar la app
ENTRYPOINT ["java", "-jar", "app.jar"]