package com.unchk.plateform_suivi_tutorat.Service;


import com.unchk.plateform_suivi_tutorat.models.Administrateur;
import com.unchk.plateform_suivi_tutorat.Repository.AdministrateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministrateurService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Administrateur saveAdministrateur(Administrateur administrateur) {
        // Crypter le mot de passe avant de sauvegarder
        administrateur.setMotDePasse(passwordEncoder.encode(administrateur.getMotDePasse()));
        return administrateurRepository.save(administrateur);
    }

    public Optional<Administrateur> getAdministrateurById(Long id) {
        return administrateurRepository.findById(id);
    }

    public void deleteAdministrateur(Long id) {
        administrateurRepository.deleteById(id);
    }
}
