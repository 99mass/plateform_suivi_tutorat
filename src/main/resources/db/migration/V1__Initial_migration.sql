CREATE TABLE Utilisateur (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(255) NOT NULL,
  prenom VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  telephone VARCHAR(20),
  mot_de_passe VARCHAR(255) NOT NULL,
  role ENUM('admin', 'tracker', 'tuteur') NOT NULL
);

CREATE TABLE Tuteur (
  id INT PRIMARY KEY,
  FOREIGN KEY (id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE Module (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(255) NOT NULL,
  nombre_semaines INT NOT NULL
);

CREATE TABLE Groupe (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(255) NOT NULL,
  module_id INT,
  FOREIGN KEY (module_id) REFERENCES Module(id) ON DELETE CASCADE
);

CREATE TABLE Seance (
  id INT AUTO_INCREMENT PRIMARY KEY,
  date DATETIME NOT NULL,
  effectuee BOOLEAN NOT NULL,
  heures_effectuees INT NOT NULL,
  heures_non_effectuees INT NOT NULL,
  tuteur_id INT,
  module_id INT,
  groupe_id INT,
  FOREIGN KEY (tuteur_id) REFERENCES Tuteur(id) ON DELETE SET NULL,
  FOREIGN KEY (module_id) REFERENCES Module(id) ON DELETE CASCADE,
  FOREIGN KEY (groupe_id) REFERENCES Groupe(id) ON DELETE CASCADE
);

CREATE TABLE TuteurModule (
  tuteur_id INT,
  module_id INT,
  PRIMARY KEY (tuteur_id, module_id),
  FOREIGN KEY (tuteur_id) REFERENCES Tuteur(id) ON DELETE CASCADE,
  FOREIGN KEY (module_id) REFERENCES Module(id) ON DELETE CASCADE
);

CREATE TABLE TuteurGroupe (
  tuteur_id INT,
  groupe_id INT,
  PRIMARY KEY (tuteur_id, groupe_id),
  FOREIGN KEY (tuteur_id) REFERENCES Tuteur(id) ON DELETE CASCADE,
  FOREIGN KEY (groupe_id) REFERENCES Groupe(id) ON DELETE CASCADE
);
