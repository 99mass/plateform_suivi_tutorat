package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.dtos.*;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import com.unchk.plateform_suivi_tutorat.services.TuteurService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/tuteurs")
public class TuteurController {

    private static final String USER_ROLE_ADMIN = Utilisateur.Role.admin.name();
    private static final String USER_ROLE_TRACKER = Utilisateur.Role.tracker.name();
    private static final String USER_FORBIDDEN_MESSAGE = "Vous n'avez pas les droits pour accéder à cette ressource";

    @Autowired
    private TuteurService tuteurService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/{tuteurId}/modules/{moduleId}")
    public ResponseEntity<Object> addModuleToTuteur(
            @PathVariable Long tuteurId,
            @PathVariable Long moduleId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                TuteurDTO updatedTuteurDTO = tuteurService.addModuleToTuteur(tuteurId, moduleId);
                return ResponseEntity.ok(updatedTuteurDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{tuteurId}/groupes/{groupeId}")
    public ResponseEntity<Object> addGroupeToTuteur(
            @PathVariable Long tuteurId,
            @PathVariable Long groupeId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                TuteurDTO updatedTuteurDTO = tuteurService.addGroupeToTuteur(tuteurId, groupeId);
                return ResponseEntity.ok(updatedTuteurDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/update-module")
    public ResponseEntity<Object> updateModuleForTuteur(
            @RequestParam Long currentTuteurId,
            @RequestParam Long currentModuleId,
            @RequestParam Long newTuteurId,
            @RequestParam Long newModuleId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                TuteurDTO updatedTuteurDTO = tuteurService.updateModuleForTuteur(currentTuteurId, currentModuleId, newTuteurId, newModuleId);
                return ResponseEntity.ok(updatedTuteurDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/update-groupe")
    public ResponseEntity<Object> updateGroupeForTuteur(
            @RequestParam Long currentTuteurId,
            @RequestParam Long currentGroupeId,
            @RequestParam Long newTuteurId,
            @RequestParam Long newGroupeId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                TuteurDTO updatedTuteurDTO = tuteurService.updateGroupeForTuteur(currentTuteurId, currentGroupeId, newTuteurId, newGroupeId);
                return ResponseEntity.ok(updatedTuteurDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{tuteurId}/modules/{moduleId}")
    public ResponseEntity<Object> removeModuleFromTuteur(
            @PathVariable Long tuteurId,
            @PathVariable Long moduleId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                TuteurDTO updatedTuteurDTO = tuteurService.removeModuleFromTuteur(tuteurId, moduleId);
                return ResponseEntity.ok(updatedTuteurDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{tuteurId}/groupes/{groupeId}")
    public ResponseEntity<Object> removeGroupeFromTuteur(
            @PathVariable Long tuteurId,
            @PathVariable Long groupeId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                TuteurDTO updatedTuteurDTO = tuteurService.removeGroupeFromTuteur(tuteurId, groupeId);
                return ResponseEntity.ok(updatedTuteurDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }


    @GetMapping("/{tuteurId}/modules")
    public ResponseEntity<TuteurWithModulesDTO> getAllModulesForTuteur(@PathVariable Long tuteurId) {
        TuteurWithModulesDTO tuteurWithModules = tuteurService.getAllModulesForTuteur(tuteurId);
        return new ResponseEntity<>(tuteurWithModules, HttpStatus.OK);
    }


    @GetMapping("/{tuteurId}/groupes")
    public ResponseEntity<TuteurWithGroupesDTO> getAllGroupesForTuteur(@PathVariable Long tuteurId) {
        TuteurWithGroupesDTO tuteurWithGroupes = tuteurService.getAllGroupesForTuteur(tuteurId);
        return new ResponseEntity<>(tuteurWithGroupes, HttpStatus.OK);
    }


    @GetMapping("/{tuteurId}/modules/{moduleId}")
    public ResponseEntity<TuteurWithModuleDTO> getModuleForTuteur(@PathVariable Long tuteurId, @PathVariable Long moduleId) {
        TuteurWithModuleDTO tuteurWithModule = tuteurService.getModuleForTuteur(tuteurId, moduleId);
        return new ResponseEntity<>(tuteurWithModule, HttpStatus.OK);
    }


    @GetMapping("/{tuteurId}/groupes/{groupeId}")
    public ResponseEntity<TuteurWithGroupeDTO> getGroupeForTuteur(@PathVariable Long tuteurId, @PathVariable Long groupeId) {
        TuteurWithGroupeDTO tuteurWithGroupe = tuteurService.getGroupeForTuteur(tuteurId, groupeId);
        return new ResponseEntity<>(tuteurWithGroupe, HttpStatus.OK);
    }
}
