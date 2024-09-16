package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.dtos.TuteurDTO;
import com.unchk.plateform_suivi_tutorat.services.TuteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tuteurs")
public class TuteurController {

    @Autowired
    private TuteurService tuteurService;

    @PostMapping("/{tuteurId}/modules/{moduleId}")
    public ResponseEntity<Object> addModuleToTuteur(@PathVariable Long tuteurId, @PathVariable Long moduleId) {
        try {
            TuteurDTO updatedTuteurDTO = tuteurService.addModuleToTuteur(tuteurId, moduleId);
            return ResponseEntity.ok(updatedTuteurDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{tuteurId}/groupes/{groupeId}")
    public ResponseEntity<Object> addGroupeToTuteur(@PathVariable Long tuteurId, @PathVariable Long groupeId) {
        try {
            TuteurDTO updatedTuteurDTO = tuteurService.addGroupeToTuteur(tuteurId, groupeId);
            return ResponseEntity.ok(updatedTuteurDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
