package com.unchk.plateform_suivi_tutorat.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class SeanceDTO {
    private Long id;
    private LocalDateTime date;
    private boolean effectuee;
    private int heuresEffectuees;
    private int heuresNonEffectuees;
    private TuteurDTO tuteur;
    private ModuleDTO module;
    private GroupeDTO groupe;
    private List<String> dates;

    // Constructors
    public SeanceDTO() {}

    public SeanceDTO(Long id, LocalDateTime date, boolean effectuee, int heuresEffectuees, int heuresNonEffectuees, TuteurDTO tuteur, ModuleDTO module, GroupeDTO groupe, List<String> dates) {
        this.id = id;
        this.date = date;
        this.effectuee = effectuee;
        this.heuresEffectuees = heuresEffectuees;
        this.heuresNonEffectuees = heuresNonEffectuees;
        this.tuteur = tuteur;
        this.module = module;
        this.groupe = groupe;
        this.dates = dates;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public boolean isEffectuee() { return effectuee; }
    public void setEffectuee(boolean effectuee) { this.effectuee = effectuee; }
    public int getHeuresEffectuees() { return heuresEffectuees; }
    public void setHeuresEffectuees(int heuresEffectuees) { this.heuresEffectuees = heuresEffectuees; }
    public int getHeuresNonEffectuees() { return heuresNonEffectuees; }
    public void setHeuresNonEffectuees(int heuresNonEffectuees) { this.heuresNonEffectuees = heuresNonEffectuees; }
    public TuteurDTO getTuteur() { return tuteur; }
    public void setTuteur(TuteurDTO tuteur) { this.tuteur = tuteur; }
    public ModuleDTO getModule() { return module; }
    public void setModule(ModuleDTO module) { this.module = module; }
    public GroupeDTO getGroupe() { return groupe; }
    public void setGroupe(GroupeDTO groupe) { this.groupe = groupe; }
    public List<String> getDates() { return dates; }
    public void setDates(List<String> dates) { this.dates = dates; }
}

