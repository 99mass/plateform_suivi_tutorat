# 🎓 Plateforme de Suivi de Tutorat

## 📜 Introduction

La **Plateforme de Suivi de Tutorat** vise à faciliter la gestion et le suivi des séances hebdomadaires effectuées par les tuteurs assignés à différents modules et groupes d'apprenants. Chaque tuteur est responsable de plusieurs groupes en fonction des modules auxquels il est affecté. L'application permet de suivre la progression des séances sur une période de 10 semaines.

## 🎯 Objectifs

- Suivi des séances hebdomadaires de tutorat (effectuées ou non effectuées).
- Gestion des tuteurs et de leurs affectations à des modules et des groupes.
- Système d'incrémentation automatique du nombre d'heures de cours effectuées ou non effectuées.
- Interface permettant à l'équipe de tracking de suivre et de valider les séances.
- Génération de rapports sur les séances effectuées par module, tuteur ou groupe.

## 🔧 Fonctionnalités

- **Gestion des utilisateurs**
    - Profils :
        - **Tuteur** : Peut consulter ses affectations, voir son historique de séances et leurs statuts.
        - **Équipe de tracking** : Peut ajouter/modifier les affectations des tuteurs et marquer les séances comme effectuées ou non effectuées.
    - Authentification : Interface de connexion pour les utilisateurs avec rôles.

- **Gestion des affectations**
    - Modules : Créer, modifier, et supprimer des modules.
    - Tuteurs : Assigner des tuteurs à des modules spécifiques.
    - Groupes : Assigner des groupes d'apprenants à des tuteurs par module.

- **Suivi des séances**
    - Calendrier des séances : Planification des séances hebdomadaires sur une durée de 10 semaines.
    - Validation des séances : L'équipe de tracking peut marquer une séance comme effectuée ou non effectuée.
    - Incrémentation automatique des heures.

- **📊 Rapports et statistiques**
    - Suivi des heures : Affichage du nombre total d'heures effectuées et non effectuées.

---

## 🌐 Architecture du Projet

L'architecture de ce projet se compose de :

- **Backend** : Application Spring Boot qui gère la logique métier et les interactions avec la base de données.
- **Frontend** : Application React avec Material UI qui offre une interface utilisateur interactive.
- **Base de données** : MySQL pour le stockage des données.

---

## ⚙️ Configuration du Fichier `application.properties`

Le fichier `application.properties` définit les configurations nécessaires pour se connecter à la base de données ainsi que d'autres paramètres, comme la gestion des tokens JWT. Voici la configuration à utiliser avec une base de données MySQL locale :

```properties
# Nom de l'application
spring.application.name=plateform_suivi_tutorat

# Connexion à la base de données MySQL locale
spring.datasource.url=jdbc:mysql://localhost:3306/plateform_suivi_tutorat
spring.datasource.username=root
spring.datasource.password=

# Configuration Hibernate (mise à jour automatique des schémas)
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

# Paramètres JWT
jwt.secret=votreCleSecreteTresLongueEtComplexeEnBase64
jwt.expiration=36000000  # Durée en millisecondes (10 heures)
```

### Changer la Clé Secrète du JWT

Pour sécuriser votre application, vous devez **modifier** la clé secrète JWT définie dans le fichier `application.properties`. Cette clé est utilisée pour signer et valider les tokens JWT. Assurez-vous de choisir une clé longue et complexe, en base64, pour une sécurité optimale :

```properties
jwt.secret=votreCleSecreteTresLongueEtComplexeEnBase64
```

Vous pouvez aussi ajuster la durée d'expiration du token JWT en millisecondes selon vos besoins. Par défaut, elle est fixée à 10 heures (36000000 ms), mais vous pouvez la modifier :

```properties
jwt.expiration=36000000
```

---

## 📋 Prérequis

Avant de commencer, vous devez avoir installé les éléments suivants sur votre machine :

- [Maven](https://maven.apache.org/) (pour exécuter le backend)
- [Node.js et npm](https://nodejs.org/) (pour exécuter le frontend)
- [MySQL](https://www.mysql.com/) (pour la base de données)

---

## 🚀 Lancer le Backend et le Frontend Sans Docker

### Backend

1. Assurez-vous d'avoir [Maven](https://maven.apache.org/) installé sur votre machine.
2. Accédez au répertoire du backend :

   ```bash
   cd plateform_suivi_tutorat_backend
   ```

3. Exécutez l'application Spring Boot :

   ```bash
   mvn spring-boot:run
   ```

### Frontend

1. Accédez au répertoire du frontend :

   ```bash
   cd plateform_suivi_tutorat_frontend
   ```

2. Installez les dépendances :

   ```bash
   npm install
   ```

3. Exécutez l'application React :

   ```bash
   npm start
   ```

---

## 🛠️ Première Connexion : Administrateur par Défaut

Lors de la première initialisation du projet, un administrateur par défaut est créé automatiquement. **Cet utilisateur doit se connecter en premier** afin de pouvoir gérer et créer d'autres utilisateurs.

### Identifiants de l'administrateur par défaut :

- **Email** : `admin@gmail.com`
- **Mot de passe** : `Passer123`

Une fois connecté avec ces informations, vous pourrez créer d'autres utilisateurs, notamment des tuteurs et des membres de l'équipe de suivi.

---

## 🔗 Swagger Link

Accédez à la documentation de l'API via Swagger à l'adresse suivante :

```
http://localhost:8080/swagger-ui/index.html
```

L'emoji "🔗" représente le lien vers Swagger pour une meilleure visibilité dans le README.

---

## 📝 Note

- Si vous souhaitez modifier les paramètres de la base de données (utilisateur, mot de passe, nom de la base), vous pouvez le faire dans le fichier `application.properties`.

---