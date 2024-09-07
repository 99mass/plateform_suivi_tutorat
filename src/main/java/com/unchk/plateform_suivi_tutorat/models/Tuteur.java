// Tuteur.java
package com.unchk.plateform_suivi_tutorat.models;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Tuteur")
public class Tuteur extends Utilisateur {
    @ManyToMany
    @JoinTable(
            name = "TuteurModule",
            joinColumns = @JoinColumn(name = "tuteur_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<Module> modules = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "TuteurGroupe",
            joinColumns = @JoinColumn(name = "tuteur_id"),
            inverseJoinColumns = @JoinColumn(name = "groupe_id")
    )
    private Set<Groupe> groupes = new HashSet<>();
}
