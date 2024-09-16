package com.unchk.plateform_suivi_tutorat.dtos;

public class GroupeDTO {
    private Long id;
    private String nom;

    // Constructors
    public GroupeDTO() {}

    public GroupeDTO(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
