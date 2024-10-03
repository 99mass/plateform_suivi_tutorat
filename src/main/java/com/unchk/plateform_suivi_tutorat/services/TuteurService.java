package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.dtos.*;
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

    public static final String TUTEUR_NOT_FOUND = "Tuteur non trouvé";
    public static final String MODULE_NOT_FOUND = "Module non trouvé";
    public static final String GROUPE_NOT_FOUND = "Groupe non trouvé";
    public static final String MODULE_ALREADY_EXISTS = "Module est déjà affecté à ce tuteur";
    public static final String GROUPE_ALREADY_EXISTS = "Groupe est déjà affecté à ce tuteur";

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    public Tuteur getTuteurById(Long id) {
        return tuteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TUTEUR_NOT_FOUND));
    }

    public TuteurDTO convertToDTO(Tuteur tuteur) {
        Set<ModuleDTO> moduleDTOs = tuteur.getModules().stream()
                .map(module -> new ModuleDTO(module.getId(), module.getNom(), module.getNombreSemaines()))
                .collect(Collectors.toSet());

        Set<GroupeDTO> groupeDTOs = tuteur.getGroupes().stream()
                .map(groupe -> new GroupeDTO(groupe.getId(), groupe.getNom(),null))
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
                .orElseThrow(() -> new EntityNotFoundException(MODULE_NOT_FOUND));

        if (tuteur.getModules().stream().anyMatch(existingModule -> existingModule.getId().equals(moduleId))) {
            throw new RuntimeException(MODULE_ALREADY_EXISTS);
        }

        tuteur.addModule(module);
        tuteurRepository.save(tuteur);
        tuteurRepository.flush();  // Ensures changes are flushed to the database
        return convertToDTO(tuteur);
    }

    @Transactional
    public TuteurDTO addGroupeToTuteur(Long tuteurId, Long groupeId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new EntityNotFoundException(GROUPE_NOT_FOUND ));

        if (tuteur.getGroupes().stream().anyMatch(existingGroupe -> existingGroupe.getId().equals(groupeId))) {
            throw new RuntimeException(GROUPE_ALREADY_EXISTS );
        }

        tuteur.addGroupe(groupe);
        tuteur = tuteurRepository.save(tuteur);
        return convertToDTO(tuteur);
    }

    @Transactional
    public TuteurDTO removeModuleFromTuteur(Long tuteurId, Long moduleId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException(MODULE_NOT_FOUND));

        if (tuteur.getModules().stream().noneMatch(existingModule -> existingModule.getId().equals(moduleId))) {
            throw new RuntimeException(MODULE_NOT_FOUND);
        }

        tuteur.removeModule(module);
        tuteurRepository.save(tuteur);
        return convertToDTO(tuteur);
    }

    @Transactional
    public TuteurDTO removeGroupeFromTuteur(Long tuteurId, Long groupeId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new EntityNotFoundException(GROUPE_NOT_FOUND));

        if (tuteur.getGroupes().stream().noneMatch(existingGroupe -> existingGroupe.getId().equals(groupeId))) {
            throw new RuntimeException(GROUPE_NOT_FOUND);
        }

        tuteur.removeGroupe(groupe);
        tuteurRepository.save(tuteur);
        return convertToDTO(tuteur);
    }

    public TuteurWithModulesDTO getAllModulesForTuteur(Long tuteurId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Set<ModuleDTO> moduleDTOs = tuteur.getModules().stream()
                .map(module -> new ModuleDTO(module.getId(), module.getNom(), module.getNombreSemaines()))
                .collect(Collectors.toSet());

        return new TuteurWithModulesDTO(
                new TuteurDTO(
                        tuteur.getId(),
                        tuteur.getNom(),
                        tuteur.getPrenom(),
                        tuteur.getEmail(),
                        tuteur.getTelephone(),
                        tuteur.getRole().name(),
                        moduleDTOs,
                        null
                ),
                moduleDTOs
        );
    }

    public TuteurWithGroupesDTO getAllGroupesForTuteur(Long tuteurId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Set<GroupeDTO> groupeDTOs = tuteur.getGroupes().stream()
                .map(groupe -> new GroupeDTO(groupe.getId(), groupe.getNom(),null))
                .collect(Collectors.toSet());

        return new TuteurWithGroupesDTO(
                new TuteurDTO(
                        tuteur.getId(),
                        tuteur.getNom(),
                        tuteur.getPrenom(),
                        tuteur.getEmail(),
                        tuteur.getTelephone(),
                        tuteur.getRole().name(),
                        null,
                        groupeDTOs
                ),
                groupeDTOs
        );
    }

    public TuteurWithModuleDTO getModuleForTuteur(Long tuteurId, Long moduleId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Module module = tuteur.getModules().stream()
                .filter(mod -> mod.getId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(MODULE_NOT_FOUND));

        return new TuteurWithModuleDTO(
                new TuteurDTO(
                        tuteur.getId(),
                        tuteur.getNom(),
                        tuteur.getPrenom(),
                        tuteur.getEmail(),
                        tuteur.getTelephone(),
                        tuteur.getRole().name(),
                        null,
                        null
                ),
                new ModuleDTO(module.getId(), module.getNom(), module.getNombreSemaines())
        );
    }

    public TuteurWithGroupeDTO getGroupeForTuteur(Long tuteurId, Long groupeId) {
        Tuteur tuteur = getTuteurById(tuteurId);
        Groupe groupe = tuteur.getGroupes().stream()
                .filter(grp -> grp.getId().equals(groupeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(GROUPE_NOT_FOUND));

        return new TuteurWithGroupeDTO(
                new TuteurDTO(
                        tuteur.getId(),
                        tuteur.getNom(),
                        tuteur.getPrenom(),
                        tuteur.getEmail(),
                        tuteur.getTelephone(),
                        tuteur.getRole().name(),
                        null,
                        null
                ),
                new GroupeDTO(groupe.getId(), groupe.getNom(),null
                )
        );
    }

    @Transactional
    public TuteurDTO updateModuleForTuteur(Long currentTuteurId, Long currentModuleId, Long newTuteurId, Long newModuleId) {

        Tuteur currentTuteur = getTuteurById(currentTuteurId);

        Module currentModule = moduleRepository.findById(currentModuleId)
                .orElseThrow(() -> new EntityNotFoundException(MODULE_NOT_FOUND));

        if (currentTuteur.getModules().stream().noneMatch(mod -> mod.getId().equals(currentModuleId))) {
            throw new RuntimeException(MODULE_NOT_FOUND);
        }

        Tuteur newTuteur = getTuteurById(newTuteurId);
        Module newModule = moduleRepository.findById(newModuleId)
                .orElseThrow(() -> new EntityNotFoundException(MODULE_NOT_FOUND));


        currentTuteur.removeModule(currentModule);
        tuteurRepository.save(currentTuteur);


        if (newTuteur.getModules().stream().anyMatch(mod -> mod.getId().equals(newModuleId))) {
            throw new RuntimeException(MODULE_ALREADY_EXISTS);
        }
        newTuteur.addModule(newModule);
        tuteurRepository.save(newTuteur);


        return convertToDTO(newTuteur);
    }

    @Transactional
    public TuteurDTO updateGroupeForTuteur(Long currentTuteurId, Long currentGroupeId, Long newTuteurId, Long newGroupeId) {

        Tuteur currentTuteur = getTuteurById(currentTuteurId);


        Groupe currentGroupe = groupeRepository.findById(currentGroupeId)
                .orElseThrow(() -> new EntityNotFoundException(GROUPE_NOT_FOUND));


        if (currentTuteur.getGroupes().stream().noneMatch(grp -> grp.getId().equals(currentGroupeId))) {
            throw new RuntimeException(GROUPE_NOT_FOUND);
        }


        Tuteur newTuteur = getTuteurById(newTuteurId);
        Groupe newGroupe = groupeRepository.findById(newGroupeId)
                .orElseThrow(() -> new EntityNotFoundException(GROUPE_NOT_FOUND));


        currentTuteur.removeGroupe(currentGroupe);
        tuteurRepository.save(currentTuteur);

        if (newTuteur.getGroupes().stream().anyMatch(grp -> grp.getId().equals(newGroupeId))) {
            throw new RuntimeException(GROUPE_ALREADY_EXISTS);
        }
        newTuteur.addGroupe(newGroupe);
        tuteurRepository.save(newTuteur);

        return convertToDTO(newTuteur);
    }


}
