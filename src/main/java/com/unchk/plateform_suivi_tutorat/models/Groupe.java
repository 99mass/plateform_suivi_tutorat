// Groupe.java
package com.unchk.plateform_suivi_tutorat.models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Groupe")
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToMany(mappedBy = "groupes")
    private Set<Tuteur> tuteurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Set<Tuteur> getTuteurs() {
        return tuteurs;
    }

    public void setTuteurs(Set<Tuteur> tuteurs) {
        this.tuteurs = tuteurs;
    }
}