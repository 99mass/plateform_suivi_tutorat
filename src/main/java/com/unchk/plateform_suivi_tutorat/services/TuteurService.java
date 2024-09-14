package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Groupe;
import com.unchk.plateform_suivi_tutorat.models.Tuteur;
import com.unchk.plateform_suivi_tutorat.models.Module;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.repositories.GroupeRepository;
import com.unchk.plateform_suivi_tutorat.repositories.ModuleRepository;
import com.unchk.plateform_suivi_tutorat.repositories.TuteurRepository;
import com.unchk.plateform_suivi_tutorat.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TuteurService {

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public void addModuleToTuteur(Long tuteurId, Long moduleId) {
        Utilisateur tuteur = utilisateurRepository.findById(tuteurId)
                .filter(user -> user.getRole() == Utilisateur.Role.tuteur)
                .orElseThrow(() -> new RuntimeException("Tuteur not found"));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        ((Tuteur) tuteur).addModule(module);  // Cast to Tuteur
        utilisateurRepository.save(tuteur);
    }

    public void addGroupeToTuteur(Long tuteurId, Long groupeId) {
        Utilisateur tuteur = utilisateurRepository.findById(tuteurId)
                .filter(user -> user.getRole() == Utilisateur.Role.tuteur)
                .orElseThrow(() -> new RuntimeException("Tuteur not found"));
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        ((Tuteur) tuteur).addGroupe(groupe);  // Cast to Tuteur
        utilisateurRepository.save(tuteur);
    }
}
