package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.services.TuteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tuteurs")
public class TuteurController {

    @Autowired
    private TuteurService tuteurService;

    // Endpoint to add a module to a Tuteur
    @PostMapping("/{tuteurId}/modules/{moduleId}")
    public ResponseEntity<String> addModuleToTuteur(@PathVariable Long tuteurId, @PathVariable Long moduleId) {
        tuteurService.addModuleToTuteur(tuteurId, moduleId);
        return ResponseEntity.ok("Module added to Tuteur successfully.");
    }

    // Endpoint to add a group to a Tuteur
    @PostMapping("/{tuteurId}/groupes/{groupeId}")
    public ResponseEntity<String> addGroupeToTuteur(@PathVariable Long tuteurId, @PathVariable Long groupeId) {
        tuteurService.addGroupeToTuteur(tuteurId, groupeId);
        return ResponseEntity.ok("Groupe added to Tuteur successfully.");
    }


}
