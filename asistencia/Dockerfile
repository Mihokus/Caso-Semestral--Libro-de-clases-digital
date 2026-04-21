# Etapa 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiamos archivos de configuración
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# CORRECCIÓN DE FORMATO: Elimina caracteres invisibles de Windows (CRLF)
# y da permiso de ejecución. Esto evita el 90% de los fallos en mvnw.
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# Descargamos dependencias
RUN ./mvnw dependency:go-offline -B

# Copiamos el código y compilamos
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]