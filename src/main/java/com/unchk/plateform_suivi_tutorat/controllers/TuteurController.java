package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.models.Tuteur;
import com.unchk.plateform_suivi_tutorat.services.TuteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tuteurs")
public class TuteurController {

    @Autowired
    private TuteurService tuteurService;

    @PostMapping("/{tuteurId}/modules/{moduleId}")
    public ResponseEntity<Tuteur> addModuleToTuteur(@PathVariable Long tuteurId, @PathVariable Long moduleId) {
        Tuteur updatedTuteur = tuteurService.addModuleToTuteur(tuteurId, moduleId);
        return ResponseEntity.ok(updatedTuteur);
    }

    @PostMapping("/{tuteurId}/groupes/{groupeId}")
    public ResponseEntity<Tuteur> addGroupeToTuteur(@PathVariable Long tuteurId, @PathVariable Long groupeId) {
        Tuteur updatedTuteur = tuteurService.addGroupeToTuteur(tuteurId, groupeId);
        return ResponseEntity.ok(updatedTuteur);
    }
}