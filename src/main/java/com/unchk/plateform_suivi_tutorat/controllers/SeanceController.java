package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.dtos.SeanceDTO;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import com.unchk.plateform_suivi_tutorat.services.SeanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    private static final String USER_ROLE_ADMIN = Utilisateur.Role.admin.name();
    private static final String USER_ROLE_TRACKER = Utilisateur.Role.tracker.name();
    private static final String USER_FORBIDDEN_MESSAGE = "Vous n'avez pas les droits pour accéder à cette ressource";

    private final SeanceService seanceService;
    private final JwtService jwtService;

    @Autowired
    public SeanceController(SeanceService seanceService, JwtService jwtService) {
        this.seanceService = seanceService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Récupérer toutes les séances", description = "Retourne une liste de toutes les séances. Accessible aux administrateurs et trackers.", responses = {
            @ApiResponse(responseCode = "200", description = "Liste des séances récupérée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires")
    })
    @GetMapping("/all")
    public ResponseEntity<Object> getAllSeances(HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                List<SeanceDTO> seances = seanceService.getAllSeances();
                return ResponseEntity.ok(seances);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer une séance par ID", description = "Retourne les détails d'une séance spécifique basée sur l'ID. Accessible aux administrateurs et trackers.", responses = {
            @ApiResponse(responseCode = "200", description = "Séance récupérée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
            @ApiResponse(responseCode = "404", description = "Séance non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getSeanceById(@PathVariable Long id, HttpServletRequest request) {
        try {
            SeanceDTO seance = seanceService.getSeanceById(id);
            return ResponseEntity.ok(seance);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer toutes les séances d'un tuteur", description = "Retourne une liste de toutes les séances associées à un tuteur donné.", responses = {
            @ApiResponse(responseCode = "200", description = "Liste des séances récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Tuteur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @GetMapping("/tuteur/{tuteurId}")
    public ResponseEntity<Object> getSeancesByTuteurId(@PathVariable Long tuteurId) {
        try {
            List<SeanceDTO> seances = seanceService.getSeancesByTuteurId(tuteurId);
            return ResponseEntity.ok(seances);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Créer une nouvelle séance", description = "Crée une nouvelle séance. Accessible uniquement aux administrateurs et trackers.", responses = {
            @ApiResponse(responseCode = "201", description = "Séance créée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createSeance(@RequestParam Long tuteurId,
            @RequestParam Long moduleId,
            @RequestParam Long groupeId,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                SeanceDTO createdSeance = seanceService.createSeance(tuteurId, moduleId, groupeId);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSeance);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Mettre à jour les informations d'une séance", description = "Met à jour les informations d'une séance telles que le tuteur, le module, le groupe, la date, les heures effectuées/non effectuées, et l'état (effectuée ou non). Accessible uniquement aux administrateurs et trackers.", responses = {
            @ApiResponse(responseCode = "200", description = "Séance mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur dans les paramètres fournis ou limite de séances atteinte"),
            @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
            @ApiResponse(responseCode = "404", description = "Séance, tuteur, module ou groupe non trouvé")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateSeanceDetails(
            @PathVariable Long id,
            @RequestBody SeanceDTO seanceDTO,
            HttpServletRequest request) {

        try {
            String role = jwtService.extractRoleFromToken(request);

            System.out.println(seanceDTO);


            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                SeanceDTO updatedSeance = seanceService.updateSeanceDetails(
                        id, seanceDTO.getTuteur().getId(), seanceDTO.getModule().getId(),
                        seanceDTO.getGroupe().getId(), seanceDTO.getDate(),
                        seanceDTO.getHeuresEffectuees(), seanceDTO.getHeuresNonEffectuees(),
                        seanceDTO.isEffectuee());
                return ResponseEntity.ok(updatedSeance);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/marked/{id}")
    public ResponseEntity<Object> markedSeanceStatus(@PathVariable Long id, @RequestParam boolean effectuee,
            HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                SeanceDTO updatedSeance = seanceService.updateSeanceStatus(id, effectuee);
                return ResponseEntity.ok(updatedSeance);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer une séance", description = "Supprime une séance par ID. Accessible uniquement aux administrateurs et trackers.", responses = {
            @ApiResponse(responseCode = "204", description = "Séance supprimée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
            @ApiResponse(responseCode = "404", description = "Séance non trouvée")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteSeance(@PathVariable Long id, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                seanceService.deleteSeance(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
