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
        validateModule(module);
        if (moduleRepository.existsByNom(module.getNom())) {
            throw new RuntimeException(ERROR_FOUND);
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
                module.setNombreSemaines(moduleDetails.getNombreSemaines());
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
