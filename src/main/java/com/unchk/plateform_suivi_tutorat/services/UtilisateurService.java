package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.Utiles.Helper;
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

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUserById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur createUser(Utilisateur utilisateur) {
        if (utilisateur.getNom() == null || utilisateur.getNom().isEmpty() ||
                utilisateur.getPrenom() == null || utilisateur.getPrenom().isEmpty()) {
            throw new RuntimeException(EMPTY_MESSAGE);
        }

        validateUtilisateur(utilisateur);

        // Encrypt the password before saving
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUser(Long id, Utilisateur utilisateurDetails) {
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
            if (utilisateurDetails.getMotDePasse() != null && !utilisateurDetails.getMotDePasse().isEmpty()) {
                if (!helper.isValidPassword(utilisateurDetails.getMotDePasse())) {
                    throw new RuntimeException(INVALID_PASSWORD_MESSAGE);
                }
                user.setMotDePasse(passwordEncoder.encode(utilisateurDetails.getMotDePasse()));
            }
            user.setRole(utilisateurDetails.getRole());
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
