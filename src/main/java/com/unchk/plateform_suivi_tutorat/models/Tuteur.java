// Tuteur.java
package com.unchk.plateform_suivi_tutorat.models;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Tuteur")
public class Tuteur extends Utilisateur {
    @ManyToMany
    @JoinTable(
            name = "TuteurModule",
            joinColumns = @JoinColumn(name = "tuteur_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
//    @JsonManagedReference
    private Set<Module> modules = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "TuteurGroupe",
            joinColumns = @JoinColumn(name = "tuteur_id"),
            inverseJoinColumns = @JoinColumn(name = "groupe_id")
    )
    @JsonManagedReference
    private Set<Groupe> groupes = new HashSet<>();

      // Relation avec Seance
      @OneToMany(mappedBy = "tuteur", cascade = CascadeType.ALL, orphanRemoval = true)
      @JsonManagedReference 
      private Set<Seance> seances = new HashSet<>();

    // Constructors
    public Tuteur() {
        super();
    }

    // Getters and Setters for `modules` and `groupes`
    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Set<Groupe> getGroupes() {
        return groupes;
    }

    public void setGroupes(Set<Groupe> groupes) {
        this.groupes = groupes;
    }

    public Set<Seance> getSeances() {
        return seances;
    }

    public void setSeances(Set<Seance> seances) {
        this.seances = seances;
    }

    // Optional: Add methods to manage the modules and groupes relationships
    public void addModule(Module module) {
        this.modules.add(module);
    }

    public void removeModule(Module module) {
        this.modules.remove(module);
    }

    public void addGroupe(Groupe groupe) {
        this.groupes.add(groupe);
    }

    public void removeGroupe(Groupe groupe) {
        this.groupes.remove(groupe);
    }


    public void addSeance(Seance seance) {
        this.seances.add(seance);
        seance.setTuteur(this); 
    }

    public void removeSeance(Seance seance) {
        this.seances.remove(seance);
        seance.setTuteur(null); 
    }
}
