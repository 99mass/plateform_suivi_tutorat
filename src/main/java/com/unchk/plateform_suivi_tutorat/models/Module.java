// Module.java
package com.unchk.plateform_suivi_tutorat.models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Module")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Integer nombreSemaines;

    @ManyToMany(mappedBy = "modules")
    private Set<Tuteur> tuteurs = new HashSet<>();
}

