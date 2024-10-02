package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Groupe;
import com.unchk.plateform_suivi_tutorat.models.Module;
import com.unchk.plateform_suivi_tutorat.repositories.GroupeRepository;
import com.unchk.plateform_suivi_tutorat.repositories.ModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupeService {

    private static final String ERROR_NOT_FOUND="Groupe non trouvé";
    public static final String ERROR_FOUND="Un groupe avec ce nom existe déjà";
    public static final String ERROR_MODULE_ASSOCIATED="Le module associé n'existe pas";
    public static final String ERROR_GROUP_NOT_EMPTY="Le nom du groupe ne peut pas etre vide.";
    public static  final  String ERROR_GROUP_UPDATE_NOT_NECCESSAIR="Les Informations sont vides";

    @Autowired
    private final GroupeRepository groupeRepository;

    @Autowired
    private final ModuleRepository moduleRepository;

    private final Validator validator;

    @Autowired
    public GroupeService(GroupeRepository groupeRepository, ModuleRepository moduleRepository) {
        this.groupeRepository = groupeRepository;
        this.moduleRepository = moduleRepository;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }

    public Optional<Groupe> getGroupeById(Long id) {
        return groupeRepository.findById(id);
    }

    public Groupe createGroupe(Groupe groupe) {

        // Vérifier si le module associé existe
        if (groupe.getModule() != null){
            if (groupe.getModule().getId() ==null){
                throw new RuntimeException(ERROR_MODULE_ASSOCIATED);
            }
            Module module = moduleRepository.findById(groupe.getModule().getId())
                    .orElseThrow(() -> new EntityNotFoundException(ERROR_MODULE_ASSOCIATED));
        }else {
            throw new RuntimeException(ERROR_MODULE_ASSOCIATED);
        }

        if (groupe.getNom().isEmpty()){
            throw new RuntimeException(ERROR_GROUP_NOT_EMPTY);
        }
        
        validateGroupe(groupe);
        if (groupeRepository.existsByNom(groupe.getNom())) {
            throw new RuntimeException(ERROR_FOUND);
        }
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupe(Long id, Groupe groupeDetails) {
        return groupeRepository.findById(id).map(groupe -> {
            if (groupeDetails.getNom() != null) {

                if (groupeDetails.getNom().isEmpty()){
                    throw new RuntimeException(ERROR_GROUP_NOT_EMPTY);
                }
                if (groupeRepository.existsByNom(groupeDetails.getNom()) && !groupeDetails.getNom().equals(groupe.getNom())) {
                    throw new RuntimeException(ERROR_FOUND);
                }
                groupe.setNom(groupeDetails.getNom());
            }
            if (groupeDetails.getModule() != null) {
                // Vérifier si le module associé existe
                if (!moduleRepository.existsById(groupeDetails.getModule().getId())) {
                    throw new RuntimeException(ERROR_MODULE_ASSOCIATED);
                }

                groupe.setModule(groupeDetails.getModule());
            }

            if (groupeDetails.getNom() == null && groupeDetails.getModule() == null) {
                throw new RuntimeException(ERROR_GROUP_UPDATE_NOT_NECCESSAIR);
            }

            return groupeRepository.save(groupe);
        }).orElseThrow(() -> new RuntimeException(ERROR_NOT_FOUND));
    }

    public void deleteGroupe(Long id) {
        if (!groupeRepository.existsById(id)) {
            throw new RuntimeException(ERROR_NOT_FOUND);
        }
        groupeRepository.deleteById(id);
    }

    private void validateGroupe(Groupe groupe) {
        Set<ConstraintViolation<Groupe>> violations = validator.validate(groupe);
        if (!violations.isEmpty()) {
            throw new RuntimeException(ERROR_GROUP_NOT_EMPTY);
        }
    }
}
