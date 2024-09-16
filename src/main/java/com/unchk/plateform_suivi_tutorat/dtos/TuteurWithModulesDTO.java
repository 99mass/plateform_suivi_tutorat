package com.unchk.plateform_suivi_tutorat.dtos;

import java.util.Set;

public class TuteurWithModulesDTO {
    private TuteurDTO tuteur;
    private Set<ModuleDTO> modules;

    public TuteurWithModulesDTO(TuteurDTO tuteur, Set<ModuleDTO> modules) {
        this.tuteur = tuteur;
        this.modules = modules;
    }

    // Getters and setters

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public void setTuteur(TuteurDTO tuteur) {
        this.tuteur = tuteur;
    }

    public Set<ModuleDTO> getModules() {
        return modules;
    }

    public void setModules(Set<ModuleDTO> modules) {
        this.modules = modules;
    }
}

