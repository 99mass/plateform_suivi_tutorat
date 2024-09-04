Pour exécuter avec Makefile le projet avec Docker et Docker Compose, voici les étapes à suivre :

### 1. Commandes pour exécuter le projet

Utilise les commandes suivantes dans ton terminal pour interagir avec Docker :

1. **Construire les images Docker** :
   ```bash
   make build
   ```
   Cela construira l'image Docker de ton application Spring Boot en fonction du `Dockerfile`.

2. **Démarrer les services** :
   ```bash
   make up
   ```
   Cela démarrera tous les services définis dans le fichier `docker-compose.yml` (Spring Boot + MySQL).

3. **Démarrer en arrière-plan** :
   Si tu veux exécuter les services en arrière-plan sans bloquer ton terminal :
   ```bash
   make up-detached
   ```

4. **Arrêter les services** :
   Pour arrêter les services après l'exécution :
   ```bash
   make down
   ```

5. **Voir les logs des services** :
   Si tu souhaites surveiller les logs en direct :
   ```bash
   make logs
   ```

6. **Nettoyer les conteneurs, volumes et images inutilisés** :
   Si tu veux nettoyer les ressources inutilisées par Docker :
   ```bash
   make clean
   ```

### 4. Accéder aux services:

- Spring Boot App: http://localhost:8080
- phpMyAdmin : http://localhost:8081
- MySQL: Port 3306,client MySQL informations :
- Hôte: localhost ou 127.0.0.1
- Port: 3306
- Utilisateur: root
- Mot de passe: breukh


# Exécuter les Migrations
```
mvn flyway:migrate
```