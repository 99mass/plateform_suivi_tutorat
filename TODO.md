# 🚀 TODO: Développement Backend avec Spring Boot

## 0. 📋 Versions et dépendances
- [X] Spring Boot: 3.1.5 
- [X] Java: 17 (LTS) /Maven
- [ ] Dépendances principales :
    - [X] spring-boot-starter-data-jpa
    - [X] spring-boot-starter-web
    - [X] spring-boot-starter-security
    - [X] mysql-connector-java
    - [X] jakarta.annotation-api
    - [X] jsonwebtoken
    - [X] javax.servlet-api
    - [X] spring-boot-starter-validation
    - [X] springdoc-openapi-starter-webmvc-ui: 2.1.0

## 1. 🏗️ Configuration du projet
- [X] Initialiser le projet Spring Boot avec Spring Initializr
    - [X] Sélectionner la version 3.3.3 de Spring Boot
    - [X] Choisir Java 17 comme version du langage
- [X] Configurer la base de données MySQL dans `application.properties`
- [X] Mettre en place la structure du projet (packages, etc.)

## 2. 📦 Modèles de données
- [X] Créer les entités JPA pour chaque table de la base de données
    - [X] Utilisateur (classe abstraite)
    - [X] Tuteur
    - [X] Module
    - [X] Groupe
    - [X] Seance
    - [X] TuteurModule
    - [X] TuteurGroupe
- [X] Ajouter les annotations JPA appropriées (@Entity, @ManyToOne, etc.)
- [X] Implémenter les relations entre les entités

## 3. 🗄️ Couche de persistance
- [X] Créer les interfaces Repository pour chaque entité
- [X] Implémenter des méthodes personnalisées si nécessaire

## 4. 🔧 Services
- [X] Créer les interfaces de service pour chaque entité
- [X] Implémenter les classes de service avec la logique métier
    - [X] GestionUtilisateurService
    - [X] ModuleService
    - [X] GroupeService
    - [ ] SeanceService
- [ ] Ajouter la logique pour la gestion des séances et le calcul des heures

## 5. 🌐 Contrôleurs REST
- [X] Créer les contrôleurs REST pour chaque entité
- [X] Implémenter les endpoints CRUD de base
- [] Ajouter des endpoints spécifiques :
    - [X] Authentification des utilisateurs
    - [X] Affectation des tuteurs aux modules et groupes
    - [ ] Validation des séances
    - [ ] Génération de rapports

## 6. 🔐 Sécurité
- [X] Configurer Spring Security
- [X] Implémenter l'authentification JWT
- [X] Définir les rôles et les autorisations
- [X] Sécuriser les endpoints en fonction des rôles

## 7. 📊 Rapports et statistiques
- [ ] Créer des services pour générer des rapports
- [ ] Implémenter des requêtes complexes pour les statistiques
- [ ] Ajouter des endpoints pour récupérer les rapports

## 8. 🧪 Tests
- [ ] Écrire des tests unitaires pour les services
- [ ] Écrire des tests d'intégration pour les contrôleurs
- [ ] Configurer et exécuter les tests avec Maven ou Gradle

## 9. 📚 Documentation API
- [X] Intégrer Swagger pour la documentation automatique de l'API
- [X] Annoter les contrôleurs et les modèles pour Swagger
- [X] Générer et vérifier la documentation Swagger

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