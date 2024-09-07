package com.unchk.plateform_suivi_tutorat.Repository;

import com.unchk.plateform_suivi_tutorat.models.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    // Ajoute ici des méthodes spécifiques si nécessaire
}