# Dockerfile
# Étape 1 : Construction de l'application
# Utilise une image Maven pour construire l'application
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Définit le répertoire de travail à l'intérieur du conteneur
WORKDIR /app

# Copie les fichiers pom.xml et les sources dans le conteneur
COPY pom.xml .
COPY src ./src

# Exécute la commande Maven pour compiler et empaqueter l'application
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
# Utilise une image JDK légère pour exécuter l'application
FROM eclipse-temurin:17-jre-alpine

# Définit le répertoire de travail
WORKDIR /app

# Copie le fichier JAR généré lors de la première étape dans l'image
COPY --from=build /app/target/*.jar app.jar

# Expose le port 8080 pour l'application Spring Boot
EXPOSE 8080

# Commande pour démarrer l'application Spring Boot
CMD ["java", "-jar", "app.jar"]
