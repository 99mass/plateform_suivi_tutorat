package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.dtos.TuteurDTO;
import com.unchk.plateform_suivi_tutorat.dtos.ModuleDTO;
import com.unchk.plateform_suivi_tutorat.dtos.GroupeDTO;
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

import java.util.Set;
import java.util.stream.Collectors;

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

    public TuteurDTO convertToDTO(Tuteur tuteur) {
        Set<ModuleDTO> moduleDTOs = tuteur.getModules().stream()
                .map(module -> new ModuleDTO(module.getId(), module.getNom(), module.getNombreSemaines()))
                .collect(Collectors.toSet());

        Set<GroupeDTO> groupeDTOs = tuteur.getGroupes().stream()
                .map(groupe -> new GroupeDTO(groupe.getId(), groupe.getNom()))
                .collect(Collectors.toSet());

        return new TuteurDTO(
                tuteur.getId(),
                tuteur.getNom(),
                tuteur.getPrenom(),
                tuteur.getEmail(),
                tuteur.getTelephone(),
                tuteur.getRole().name(),
                moduleDTOs,
                groupeDTOs
        );
    }

    @Transactional
    public TuteurDTO addModuleToTuteur(Long tuteurId, Long moduleId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found with id: " + moduleId));
        tuteur.addModule(module);
        tuteurRepository.save(tuteur);
        tuteurRepository.flush();  // Ensures changes are flushed to the database
        return convertToDTO(tuteur);
    }

    @Transactional
    public TuteurDTO addGroupeToTuteur(Long tuteurId, Long groupeId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id: " + groupeId));
        tuteur.addGroupe(groupe);
        tuteur = tuteurRepository.save(tuteur);
        return convertToDTO(tuteur);
    }
}
