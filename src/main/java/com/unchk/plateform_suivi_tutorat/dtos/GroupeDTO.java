package com.unchk.plateform_suivi_tutorat.dtos;

public class GroupeDTO {
    private Long id;
    private String nom;
    private ModuleDTO module;

    // Constructors
    public GroupeDTO() {}

    public GroupeDTO(Long id, String nom, ModuleDTO module) {
        this.id = id;
        this.nom = nom;
        this.module = module; // Initialise le module
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public ModuleDTO getModule() { return module; } // Getter pour le module
    public void setModule(ModuleDTO module) { this.module = module; }
}
