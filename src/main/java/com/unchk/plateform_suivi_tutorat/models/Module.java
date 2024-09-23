// Module.java
package com.unchk.plateform_suivi_tutorat.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    @Min(value = 1, message = "Le nombre de semaines doit être supérieur ou égal à 1")
    private Integer nombreSemaines;

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

    public Integer getNombreSemaines() {
        return nombreSemaines;
    }

    public void setNombreSemaines(Integer nombreSemaines) {
        this.nombreSemaines = nombreSemaines;
    }


}

