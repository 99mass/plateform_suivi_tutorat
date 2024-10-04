package com.unchk.plateform_suivi_tutorat.repositories;

import com.unchk.plateform_suivi_tutorat.models.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    boolean existsByNom(String nom);
    Optional<Groupe> findByNom(String nom);
}
