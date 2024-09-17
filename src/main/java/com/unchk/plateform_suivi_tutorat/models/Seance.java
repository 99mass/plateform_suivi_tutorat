// Seance.java
package com.unchk.plateform_suivi_tutorat.models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "tuteur_id")
    private Tuteur tuteur;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;

    

}