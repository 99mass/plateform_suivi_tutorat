package com.unchk.plateform_suivi_tutorat.dtos;

import java.util.Set;

public class ModuleDTO {
    private Long id;
    private String nom;
    private int nombreSemaines;

    // Constructors
    public ModuleDTO() {}

    public ModuleDTO(Long id, String nom, int nombreSemaines) {
        this.id = id;
        this.nom = nom;
        this.nombreSemaines = nombreSemaines;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
