package com.unchk.plateform_suivi_tutorat.dtos;

public class TuteurWithModuleDTO {
    private TuteurDTO tuteur;
    private ModuleDTO module;

    public TuteurWithModuleDTO(TuteurDTO tuteur, ModuleDTO module) {
        this.tuteur = tuteur;
        this.module = module;
    }

    // Getters and setters

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public void setTuteur(TuteurDTO tuteur) {
        this.tuteur = tuteur;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }
}

