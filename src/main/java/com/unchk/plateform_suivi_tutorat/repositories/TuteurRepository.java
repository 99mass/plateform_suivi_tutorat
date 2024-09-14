package com.unchk.plateform_suivi_tutorat.repositories;

import com.unchk.plateform_suivi_tutorat.models.Tuteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuteurRepository extends JpaRepository<Tuteur, Long> {

}
