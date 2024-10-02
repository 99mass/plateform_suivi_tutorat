package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.dtos.*;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import com.unchk.plateform_suivi_tutorat.services.TuteurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @Operation(summary = "Ajouter un module à un tuteur",
            description = "Ajoute un module spécifique à un tuteur. Seuls les administrateurs et les trackers peuvent accéder à cette ressource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module ajouté avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurDTO.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans la requête",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Ajouter un groupe à un tuteur",
            description = "Ajoute un groupe spécifique à un tuteur. Seuls les administrateurs et les trackers peuvent accéder à cette ressource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe ajouté avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurDTO.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans la requête",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Mettre à jour un module pour un tuteur",
            description = "Met à jour les modules assignés à un tuteur. Seuls les administrateurs et les trackers peuvent accéder à cette ressource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurDTO.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans la requête",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Mettre à jour un groupe pour un tuteur",
            description = "Met à jour les groupes assignés à un tuteur. Seuls les administrateurs et les trackers peuvent accéder à cette ressource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurDTO.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans la requête",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Supprimer un module d'un tuteur",
            description = "Supprime un module assigné à un tuteur. Seuls les administrateurs et les trackers peuvent accéder à cette ressource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module supprimé avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurDTO.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans la requête",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Supprimer un groupe d'un tuteur",
            description = "Supprime un groupe assigné à un tuteur. Seuls les administrateurs et les trackers peuvent accéder à cette ressource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe supprimé avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurDTO.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur dans la requête",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Obtenir tous les modules d'un tuteur",
            description = "Retourne la liste de tous les modules assignés à un tuteur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des modules retournée avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurWithModulesDTO.class)))
    })
    public ResponseEntity<TuteurWithModulesDTO> getAllModulesForTuteur(@PathVariable Long tuteurId) {
        TuteurWithModulesDTO tuteurWithModules = tuteurService.getAllModulesForTuteur(tuteurId);
        return new ResponseEntity<>(tuteurWithModules, HttpStatus.OK);
    }

    @GetMapping("/{tuteurId}/groupes")
    @Operation(summary = "Obtenir tous les groupes d'un tuteur",
            description = "Retourne la liste de tous les groupes assignés à un tuteur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des groupes retournée avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurWithGroupesDTO.class)))
    })
    public ResponseEntity<TuteurWithGroupesDTO> getAllGroupesForTuteur(@PathVariable Long tuteurId) {
        TuteurWithGroupesDTO tuteurWithGroupes = tuteurService.getAllGroupesForTuteur(tuteurId);
        return new ResponseEntity<>(tuteurWithGroupes, HttpStatus.OK);
    }

    @GetMapping("/{tuteurId}/modules/{moduleId}")
    @Operation(summary = "Obtenir un module spécifique pour un tuteur",
            description = "Retourne les détails d'un module assigné à un tuteur spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module retourné avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurWithModuleDTO.class)))
    })
    public ResponseEntity<TuteurWithModuleDTO> getModuleForTuteur(@PathVariable Long tuteurId, @PathVariable Long moduleId) {
        TuteurWithModuleDTO tuteurWithModule = tuteurService.getModuleForTuteur(tuteurId, moduleId);
        return new ResponseEntity<>(tuteurWithModule, HttpStatus.OK);
    }

    @GetMapping("/{tuteurId}/groupes/{groupeId}")
    @Operation(summary = "Obtenir un groupe spécifique pour un tuteur",
            description = "Retourne les détails d'un groupe assigné à un tuteur spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe retourné avec succès",
                    content = @Content(schema = @Schema(implementation = TuteurWithGroupeDTO.class)))
    })
    public ResponseEntity<TuteurWithGroupeDTO> getGroupeForTuteur(@PathVariable Long tuteurId, @PathVariable Long groupeId) {
        TuteurWithGroupeDTO tuteurWithGroupe = tuteurService.getGroupeForTuteur(tuteurId, groupeId);
        return new ResponseEntity<>(tuteurWithGroupe, HttpStatus.OK);
    }
}
