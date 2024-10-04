package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Tuteur;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.Utiles.Helper;
import com.unchk.plateform_suivi_tutorat.repositories.TuteurRepository;
import com.unchk.plateform_suivi_tutorat.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    public static final String USER_NOT_FOUND_MESSAGE = "Utilisateur non trouvé";
    public static final String INVALID_EMAIL_MESSAGE = "L'email n'est pas valide";
    public static final String INVALID_TELEPHONE_MESSAGE = "Le numéro de téléphone n'est pas valide";
    public static final String INVALID_PASSWORD_MESSAGE = "Le mot de passe doit contenir au moins 6 caractères";
    public static final String EMPTY_MESSAGE = "Toutes les informations doivent être fournies";
    public static final String USER_DELETE_ERROR_MESSAGE = "Erreur lors de la suppression de l'utilisateur";
    public static final String USER_EXIST_MESSAGE = "Un utilisateur avec cet email existe déjà";
    public static final String ROLE_UPDATE_FORBIDDEN = "Modification du rôle non autorisée";

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUserById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Optional<Utilisateur> getUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Utilisateur createUser(Utilisateur utilisateur) {
        if (utilisateur.getNom() == null || utilisateur.getNom().isEmpty() ||
                utilisateur.getPrenom() == null || utilisateur.getPrenom().isEmpty()) {
            throw new RuntimeException(EMPTY_MESSAGE);
        }

        // Vérifiez si l'email existe déjà dans la base de données
        Optional<Utilisateur> existingUser = utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException(USER_EXIST_MESSAGE);
        }

        validateUtilisateur(utilisateur);

        // Encrypt the password before saving
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));

        // Si l'utilisateur est un tuteur, le créer en tant que Tuteur
        if (utilisateur.getRole() == Utilisateur.Role.tuteur) {
            Tuteur tuteur = new Tuteur();
            tuteur.setNom(utilisateur.getNom());
            tuteur.setPrenom(utilisateur.getPrenom());
            tuteur.setEmail(utilisateur.getEmail());
            tuteur.setTelephone(utilisateur.getTelephone());
            tuteur.setMotDePasse(utilisateur.getMotDePasse());
            tuteur.setRole(Utilisateur.Role.tuteur); // Définir le rôle du tuteur

            return tuteurRepository.save(tuteur);
        }

        // Si ce n'est pas un tuteur, sauvegarder comme un utilisateur normal
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUser(Long id, Utilisateur utilisateurDetails, boolean isAdmin, String oldPassword,
            String newPassword) {
        Helper helper = new Helper();

        return utilisateurRepository.findById(id).map(user -> {
            if (utilisateurDetails.getNom() != null && !utilisateurDetails.getNom().isEmpty()) {
                user.setNom(utilisateurDetails.getNom());
            }
            if (utilisateurDetails.getPrenom() != null && !utilisateurDetails.getPrenom().isEmpty()) {
                user.setPrenom(utilisateurDetails.getPrenom());
            }
            if (utilisateurDetails.getEmail() != null && !utilisateurDetails.getEmail().isEmpty()) {
                if (!helper.isValidEmail(utilisateurDetails.getEmail())) {
                    throw new RuntimeException(INVALID_EMAIL_MESSAGE);
                }
                user.setEmail(utilisateurDetails.getEmail());
            }
            if (utilisateurDetails.getTelephone() != null && !utilisateurDetails.getTelephone().isEmpty()) {
                if (!helper.isValidTelephone(utilisateurDetails.getTelephone())) {
                    throw new RuntimeException(INVALID_TELEPHONE_MESSAGE);
                }
                user.setTelephone(utilisateurDetails.getTelephone());
            }
            if (utilisateurDetails.getRole() != null && !utilisateurDetails.getRole().equals(user.getRole())) {
                if (!isAdmin) {
                    throw new RuntimeException(ROLE_UPDATE_FORBIDDEN);
                }
                user.setRole(utilisateurDetails.getRole());
            }
            if (oldPassword != null && newPassword != null && !oldPassword.isEmpty() && !newPassword.isEmpty()) {
                if (!passwordEncoder.matches(oldPassword, user.getMotDePasse())) {
                    throw new RuntimeException("Ancien mot de passe incorrect");
                }
                if (!helper.isValidPassword(newPassword)) {
                    throw new RuntimeException(INVALID_PASSWORD_MESSAGE);
                }
                user.setMotDePasse(passwordEncoder.encode(newPassword));
            }

            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
    }

    public void deleteUser(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException(USER_NOT_FOUND_MESSAGE);
        }
        utilisateurRepository.deleteById(id);
    }

    private void validateUtilisateur(Utilisateur utilisateur) {
        Helper helper = new Helper();

        if (!helper.isValidEmail(utilisateur.getEmail())) {
            throw new RuntimeException(INVALID_EMAIL_MESSAGE);
        }
        if (!helper.isValidTelephone(utilisateur.getTelephone())) {
            throw new RuntimeException(INVALID_TELEPHONE_MESSAGE);
        }
        if (!helper.isValidPassword(utilisateur.getMotDePasse())) {
            throw new RuntimeException(INVALID_PASSWORD_MESSAGE);
        }
    }
}
