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
}