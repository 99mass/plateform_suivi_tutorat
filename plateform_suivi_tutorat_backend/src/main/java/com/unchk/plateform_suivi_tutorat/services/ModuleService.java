package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Module;
import com.unchk.plateform_suivi_tutorat.repositories.ModuleRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    public static final String ERROR_NOT_FOUND="Module non trouvé";
    private static final String ERROR_FOUND="Un module avec ce nom existe déjà";
    private static final String ERROR_NOT_EMPTY="Un module ne peut pas etre vide";
    private static final String ERROR_EMPTY_CHAMPS="Au moins une des informations doit etre modifier";
    private static final String ERROR_Type="Le nombre de semaine ne peut etre inférieur a 0";


    @Autowired
    private final ModuleRepository moduleRepository;

    private final Validator validator;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    public Module createModule(Module module) {

        if (module.getNom().isEmpty()){
            throw new RuntimeException(ERROR_NOT_EMPTY);
        }
        validateModule(module);
        if ( moduleRepository.existsByNom(module.getNom())) {
            throw new RuntimeException(ERROR_FOUND);
        }
        if (module.getNombreSemaines()<0){
            throw new RuntimeException(ERROR_Type);
        }
        return moduleRepository.save(module);
    }

    public Module updateModule(Long id, Module moduleDetails) {
        return moduleRepository.findById(id).map(module -> {
            if (moduleDetails.getNom() != null) {
                if (moduleRepository.existsByNom(moduleDetails.getNom()) && !moduleDetails.getNom().equals(module.getNom())) {
                    throw new RuntimeException(ERROR_FOUND);
                }
                module.setNom(moduleDetails.getNom());
            }

            if (moduleDetails.getNombreSemaines() != null) {
                if (moduleDetails.getNombreSemaines()<0){
                    throw new RuntimeException(ERROR_Type);
                }
                module.setNombreSemaines(moduleDetails.getNombreSemaines());
            }

            if (moduleDetails.getNom() == null && moduleDetails.getNombreSemaines() == null){
                throw new RuntimeException(ERROR_EMPTY_CHAMPS);
            }

            return moduleRepository.save(module);
        }).orElseThrow(() -> new RuntimeException(ERROR_NOT_FOUND));
    }

    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new RuntimeException(ERROR_NOT_FOUND);
        }
        moduleRepository.deleteById(id);
    }

    private void validateModule(Module module) {
        var violations = validator.validate(module);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
