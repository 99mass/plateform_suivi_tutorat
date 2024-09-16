package com.unchk.plateform_suivi_tutorat.dtos;

public class TuteurWithGroupeDTO {
    private TuteurDTO tuteur;
    private GroupeDTO groupe;

    public TuteurWithGroupeDTO(TuteurDTO tuteur, GroupeDTO groupe) {
        this.tuteur = tuteur;
        this.groupe = groupe;
    }

    // Getters and setters

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public void setTuteur(TuteurDTO tuteur) {
        this.tuteur = tuteur;
    }

    public GroupeDTO getGroupe() {
        return groupe;
    }

    public void setGroupe(GroupeDTO groupe) {
        this.groupe = groupe;
    }
}

