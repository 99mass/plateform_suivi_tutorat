# 🚀 TODO: Développement Backend avec Spring Boot

## 0. 📋 Versions et dépendances
- [X] Spring Boot: 3.3.3 (dernière version stable à la date de septembre 2024)
- [X] Java: 17 (LTS) /Maven
- [ ] Dépendances principales:
    - [X] spring-boot-starter-data-jpa
    - [X] spring-boot-starter-web
    - [X] spring-boot-starter-security
    - [X] mysql-connector-java
    - [ ] jsonwebtoken: 0.12.3
    - [ ] springdoc-openapi-starter-webmvc-ui: 2.3.0

## 1. 🏗️ Configuration du projet
- [X] Initialiser le projet Spring Boot avec Spring Initializr
    - [X] Sélectionner la version 3.3.3 de Spring Boot
    - [X] Choisir Java 17 comme version du langage
- [ ] Configurer la base de données MySQL dans `application.properties`
- [ ] Mettre en place la structure du projet (packages, etc.)

## 2. 📦 Modèles de données
- [ ] Créer les entités JPA pour chaque table de la base de données
    - [ ] Utilisateur (classe abstraite)
    - [ ] Administrateur
    - [ ] EquipeTracking
    - [ ] Tuteur
    - [ ] Module
    - [ ] Groupe
    - [ ] Seance
    - [ ] TuteurModule
    - [ ] TuteurGroupe
- [ ] Ajouter les annotations JPA appropriées (@Entity, @ManyToOne, etc.)
- [ ] Implémenter les relations entre les entités

## 3. 🗄️ Couche de persistance
- [ ] Créer les interfaces Repository pour chaque entité
- [ ] Implémenter des méthodes personnalisées si nécessaire

## 4. 🔧 Services
- [ ] Créer les interfaces de service pour chaque entité
- [ ] Implémenter les classes de service avec la logique métier
    - [ ] GestionUtilisateurService
    - [ ] ModuleService
    - [ ] GroupeService
    - [ ] SeanceService
- [ ] Ajouter la logique pour la gestion des séances et le calcul des heures

## 5. 🌐 Contrôleurs REST
- [ ] Créer les contrôleurs REST pour chaque entité
- [ ] Implémenter les endpoints CRUD de base
- [ ] Ajouter des endpoints spécifiques :
    - [ ] Authentification des utilisateurs
    - [ ] Affectation des tuteurs aux modules et groupes
    - [ ] Validation des séances
    - [ ] Génération de rapports

## 6. 🔐 Sécurité
- [ ] Configurer Spring Security
- [ ] Implémenter l'authentification JWT
- [ ] Définir les rôles et les autorisations
- [ ] Sécuriser les endpoints en fonction des rôles

## 7. 📊 Rapports et statistiques
- [ ] Créer des services pour générer des rapports
- [ ] Implémenter des requêtes complexes pour les statistiques
- [ ] Ajouter des endpoints pour récupérer les rapports

## 8. 🧪 Tests
- [ ] Écrire des tests unitaires pour les services
- [ ] Écrire des tests d'intégration pour les contrôleurs
- [ ] Configurer et exécuter les tests avec Maven ou Gradle

## 9. 📚 Documentation API
- [ ] Intégrer Swagger pour la documentation automatique de l'API
- [ ] Annoter les contrôleurs et les modèles pour Swagger
- [ ] Générer et vérifier la documentation Swagger

## 10. 🐳 Déploiement
- [ ] Créer un Dockerfile pour le projet
- [ ] Configurer les profils Spring pour différents environnements
- [ ] Préparer les scripts de déploiement

## 11. 🔍 Logging et monitoring
- [ ] Configurer le logging avec SLF4J et Logback
- [ ] Implémenter des métriques personnalisées
- [ ] Intégrer un outil de monitoring (ex: Spring Boot Actuator)

## 12. 🌟 Optimisation et finalisation
- [ ] Optimiser les requêtes de base de données
- [ ] Implémenter la pagination pour les listes longues
- [ ] Revoir et nettoyer le code
- [ ] Effectuer des tests de charge