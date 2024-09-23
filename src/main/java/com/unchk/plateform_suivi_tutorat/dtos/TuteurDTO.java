package com.unchk.plateform_suivi_tutorat.dtos;

import java.util.Set;

public class TuteurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private Set<ModuleDTO> modules;
    private Set<GroupeDTO> groupes;

    // Constructors
    public TuteurDTO() {}

    public TuteurDTO(Long id, String nom, String prenom, String email, String telephone, String role, Set<ModuleDTO> modules, Set<GroupeDTO> groupes) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.modules = modules;
        this.groupes = groupes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public void setModules(Set<ModuleDTO> modules) { this.modules = modules; }
    public void setGroupes(Set<GroupeDTO> groupes) { this.groupes = groupes; }
}
