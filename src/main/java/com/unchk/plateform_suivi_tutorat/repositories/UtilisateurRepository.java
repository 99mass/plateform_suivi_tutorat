package com.unchk.plateform_suivi_tutorat.repositories;

import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
}