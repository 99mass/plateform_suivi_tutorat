package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Tuteur;
import com.unchk.plateform_suivi_tutorat.models.Module;
import com.unchk.plateform_suivi_tutorat.models.Groupe;
import com.unchk.plateform_suivi_tutorat.repositories.TuteurRepository;
import com.unchk.plateform_suivi_tutorat.repositories.ModuleRepository;
import com.unchk.plateform_suivi_tutorat.repositories.GroupeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class TuteurService {

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    public Tuteur getTuteurById(Long id) {
        return tuteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tuteur not found with id: " + id));
    }

    // Additional operations
    @Transactional
    public Tuteur addModuleToTuteur(Long tuteurId, Long moduleId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found with id: " + moduleId));
        tuteur.addModule(module);
        return tuteurRepository.save(tuteur);
    }

    @Transactional
    public Tuteur addGroupeToTuteur(Long tuteurId, Long groupeId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id: " + groupeId));
        tuteur.addGroupe(groupe);
        return tuteurRepository.save(tuteur);
    }
}