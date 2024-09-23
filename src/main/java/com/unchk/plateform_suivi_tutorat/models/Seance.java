package com.unchk.plateform_suivi_tutorat.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Seance")
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean effectuee;

    @Column(nullable = false)
    private Integer heuresEffectuees;

    @Column(nullable = false)
    private Integer heuresNonEffectuees;

    @ManyToOne
    @JoinColumn(name = "tuteur_id", nullable = false)
    private Tuteur tuteur;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne
    @JoinColumn(name = "groupe_id", nullable = false)
    private Groupe groupe;

    @ElementCollection
    private List<String> dates = new ArrayList<>();

    // Constructeur par défaut
    public Seance() {
    }

    // Constructeur avec paramètres
    public Seance(LocalDateTime date, boolean effectuee, int heuresEffectuees, int heuresNonEffectuees, Tuteur tuteur,
            Module module, Groupe groupe) {
        this.date = date;
        this.effectuee = effectuee;
        this.heuresEffectuees = heuresEffectuees;
        this.heuresNonEffectuees = heuresNonEffectuees;
        this.tuteur = tuteur;
        this.module = module;
        this.groupe = groupe;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEffectuee() {
        return effectuee;
    }

    public void setEffectuee(boolean effectuee) {
        this.effectuee = effectuee;
    }

    public int getHeuresEffectuees() {
        return heuresEffectuees;
    }

    public void setHeuresEffectuees(int heuresEffectuees) {
        this.heuresEffectuees = heuresEffectuees;
    }

    public int getHeuresNonEffectuees() {
        return heuresNonEffectuees;
    }

    public void setHeuresNonEffectuees(int heuresNonEffectuees) {
        this.heuresNonEffectuees = heuresNonEffectuees;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Boolean getEffectuee() {
        return effectuee;
    }

    public void setEffectuee(Boolean effectuee) {
        this.effectuee = effectuee;
    }

    public void setHeuresEffectuees(Integer heuresEffectuees) {
        this.heuresEffectuees = heuresEffectuees;
    }

    public void setHeuresNonEffectuees(Integer heuresNonEffectuees) {
        this.heuresNonEffectuees = heuresNonEffectuees;
    }

    public Tuteur getTuteur() {
        return tuteur;
    }

    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public void setDate(LocalDateTime dateTime) {
        this.date=dateTime;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    // Ajoutez une méthode pour ajouter une date
    public void addDate(String date) {
        this.dates.add(date);
    }


}
