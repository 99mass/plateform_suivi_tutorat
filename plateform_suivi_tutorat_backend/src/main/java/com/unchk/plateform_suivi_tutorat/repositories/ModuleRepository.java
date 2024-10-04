package com.unchk.plateform_suivi_tutorat.repositories;

import com.unchk.plateform_suivi_tutorat.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    boolean existsByNom(String nom);
    Optional<Module> findByNom(String nom);
}
