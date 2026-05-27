# Etapa 1: Construcción (Maven Build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiamos archivos de configuración de Maven para aprovechar la caché
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Corregimos formato de fin de línea y damos permisos
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# Descargamos dependencias sin compilar el código aún
RUN ./mvnw dependency:go-offline -B

# Copiamos el código fuente y compilamos el JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen de Ejecución (Runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto definido en application.properties
EXPOSE 8081

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]