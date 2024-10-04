package com.unchk.plateform_suivi_tutorat.dtos;

import java.util.Set;

public class TuteurWithGroupesDTO {
    private TuteurDTO tuteur;
    private Set<GroupeDTO> groupes;

    public TuteurWithGroupesDTO(TuteurDTO tuteur, Set<GroupeDTO> groupes) {
        this.tuteur = tuteur;
        this.groupes = groupes;
    }

    // Getters and setters

    public TuteurDTO getTuteur() {
        return tuteur;
    }

    public void setTuteur(TuteurDTO tuteur) {
        this.tuteur = tuteur;
    }

    public Set<GroupeDTO> getGroupes() {
        return groupes;
    }

    public void setGroupes(Set<GroupeDTO> groupes) {
        this.groupes = groupes;
    }
}
