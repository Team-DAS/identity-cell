# Paso 1: Usar una imagen base oficial de Java 17
FROM eclipse-temurin:21-jdk-jammy

# Paso 2: Argumento para especificar la ruta del archivo JAR
# Maven lo coloca en la carpeta 'target', Gradle en 'build/libs'
ARG JAR_FILE=build/libs/*.jar

# Paso 3: Copiar el archivo JAR compilado al contenedor con un nombre genérico
COPY ${JAR_FILE} app.jar

# Paso 4: Comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=prod"]