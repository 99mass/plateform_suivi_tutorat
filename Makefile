# Makefile
# Construire les images Docker
build:
	docker-compose build

# Démarrer les services (Spring Boot + MySQL)
up:
	docker-compose up

# Démarrer les services en arrière-plan
up-detached:
	docker-compose up -d

# Arrêter les services
down:
	docker-compose down

# Afficher les logs des services
logs:
	docker-compose logs -f

# Nettoyer les volumes, images et conteneurs inutilisés
clean:
	docker system prune -f
