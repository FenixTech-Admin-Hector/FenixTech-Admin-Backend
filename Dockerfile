# Usamos la imagen oficial de Eclipse Temurin con Java 21 y Alpine Linux (súper ligera)
FROM eclipse-temurin:21-jdk-alpine

# Creamos un volumen temporal que Spring Boot (Tomcat) necesita para ser más rápido
VOLUME /tmp

# Copiamos el archivo compilado .jar desde tu carpeta target hacia el contenedor
COPY target/*.jar app.jar

# El comando que ejecutará el contenedor al encenderse
ENTRYPOINT ["java","-jar","/app.jar"]
