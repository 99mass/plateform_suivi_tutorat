package com.unchk.plateform_suivi_tutorat.repositories;

import com.unchk.plateform_suivi_tutorat.models.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
  
    List<Seance> findByTuteurId(Long tuteurId);
    List<Seance> findByModuleId(Long moduleId);
    List<Seance> findByGroupeId(Long groupeId);
    boolean existsByTuteurIdAndModuleIdAndGroupeId(Long tuteurId, Long moduleId, Long groupeId);
}
