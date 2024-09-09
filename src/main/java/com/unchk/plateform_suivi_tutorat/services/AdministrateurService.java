package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Administrateur;
import com.unchk.plateform_suivi_tutorat.Utiles.Helper;
import com.unchk.plateform_suivi_tutorat.repositories.AdministrateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurService {

    public static final String ADMIN_NOT_FOUND_MESSAGE = "Administrateur non trouvé";
    public static final String INVALID_EMAIL_MESSAGE = "L'email n'est pas valide";
    public static final String INVALID_TELEPHONE_MESSAGE = "Le numéro de téléphone n'est pas valide";
    public static final String INVALID_PASSWORD_MESSAGE = "Le mot de passe doit contenir au moins 6 caractères";
    public static final String EMPTY_MESSAGE = "Tous les informations doivent être fournie";
    public static final String ADMIN_DELETE_ERROR_MESSAGE = "Erreur lors de la suppression de l'administrateur";


    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Administrateur> getAllAdmins() {
        return administrateurRepository.findAll();
    }

    public Optional<Administrateur> getAdminById(Long id) {
        return administrateurRepository.findById(id);
    }

    public Administrateur createAdmin(Administrateur administrateur) {

        if (administrateur.getNom() == null || administrateur.getNom().isEmpty() ||
            administrateur.getPrenom() ==null || administrateur.getPrenom().isEmpty()
        ) {
           throw new RuntimeException(EMPTY_MESSAGE);
        }

        validateAdministrateur(administrateur);

        // Encrypt the password before saving
        administrateur.setMotDePasse(passwordEncoder.encode(administrateur.getMotDePasse()));
        return administrateurRepository.save(administrateur);
    }

    public Administrateur updateAdmin(Long id, Administrateur administrateurDetails) {

        Helper helper = new Helper();

        return administrateurRepository.findById(id).map(admin -> {
            if (administrateurDetails.getNom() != null && !administrateurDetails.getNom().isEmpty()) {
                admin.setNom(administrateurDetails.getNom());
            }
            if (administrateurDetails.getPrenom() != null && !administrateurDetails.getPrenom().isEmpty()) {
                admin.setPrenom(administrateurDetails.getPrenom());
            }
            if (administrateurDetails.getEmail() != null && !administrateurDetails.getEmail().isEmpty()) {
                if (helper.isValidEmail(administrateurDetails.getEmail())) {
                    throw new RuntimeException(INVALID_EMAIL_MESSAGE);
                }
                admin.setEmail(administrateurDetails.getEmail());
            }
            if (administrateurDetails.getTelephone() != null && !administrateurDetails.getTelephone().isEmpty()  ) {
                if (helper.isValidTelephone(administrateurDetails.getTelephone())) {
                    throw new RuntimeException(INVALID_TELEPHONE_MESSAGE);
                }
                admin.setTelephone(administrateurDetails.getTelephone());
            }
            // Encrypt the password if it has been changed
            if (administrateurDetails.getMotDePasse() != null && !administrateurDetails.getMotDePasse().isEmpty()) {
                if (helper.isValidPassword(administrateurDetails.getMotDePasse())) {
                    throw new RuntimeException(INVALID_PASSWORD_MESSAGE);
                }
                admin.setMotDePasse(passwordEncoder.encode(administrateurDetails.getMotDePasse()));
            }
            return administrateurRepository.save(admin);
        }).orElseThrow(() -> new RuntimeException(ADMIN_NOT_FOUND_MESSAGE));
    }

    public void deleteAdmin(Long id) {
        administrateurRepository.deleteById(id);
    }

    private void validateAdministrateur(Administrateur administrateur) {
        Helper helper = new Helper();

        if (helper.isValidEmail(administrateur.getEmail())) {
            throw new RuntimeException(INVALID_EMAIL_MESSAGE);
        }
        if (helper.isValidTelephone(administrateur.getTelephone())) {
            throw new RuntimeException(INVALID_TELEPHONE_MESSAGE);
        }
        if (helper.isValidPassword(administrateur.getMotDePasse())) {
            throw new RuntimeException(INVALID_PASSWORD_MESSAGE);
        }
    }
}