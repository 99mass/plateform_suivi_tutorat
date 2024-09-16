package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.models.Groupe;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.GroupeService;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groupes")
public class GroupeController {

    public static final String USER_ROLE_ADMIN = Utilisateur.Role.admin.name();
    public static final String USER_ROLE_TRACKER = Utilisateur.Role.tracker.name();
    public static final String USER_ROLE_TUTEUR = Utilisateur.Role.tuteur.name();
    public static final String USER_FORBIDDEN_MESSAGE = "Vous n'avez pas les droits pour accéder à cette ressource";


    private final GroupeService groupeService;
    private final JwtService jwtService;

    @Autowired
    public GroupeController(GroupeService groupeService, JwtService jwtService) {
        this.groupeService = groupeService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Récupérer tous les groupes",
            description = "Retourne une liste de tous les groupes disponibles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des groupes récupérée avec succès"),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur")
            })
    @GetMapping
    public ResponseEntity<List<Groupe>> getAllGroupes() {
        List<Groupe> groupes = groupeService.getAllGroupes();
        return new ResponseEntity<>(groupes, HttpStatus.OK);
    }

    @Operation(summary = "Récupérer un groupe par ID",
            description = "Retourne les détails d'un groupe spécifique basé sur l'ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Groupe récupéré avec succès"),
                    @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Groupe> getGroupeById(@PathVariable Long id) {
        Optional<Groupe> groupe = groupeService.getGroupeById(id);
        return groupe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Créer un nouveau groupe",
            description = "Crée un nouveau groupe. Accessible uniquement aux administrateurs et trackers.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Groupe créé avec succès"),
                    @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur")
            })
    @PostMapping("/create")
    public ResponseEntity<Object> createGroupe(@RequestBody Groupe groupe, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                Groupe createdGroupe = groupeService.createGroupe(groupe);
                return new ResponseEntity<>(createdGroupe, HttpStatus.CREATED);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Mettre à jour un groupe",
            description = "Met à jour les informations d'un groupe existant. Accessible uniquement aux administrateurs et trackers.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Groupe mis à jour avec succès"),
                    @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
                    @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGroupe(@PathVariable Long id, @RequestBody Groupe groupeDetails, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                Groupe updatedGroupe = groupeService.updateGroupe(id, groupeDetails);
                return ResponseEntity.ok(updatedGroupe);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer un groupe",
            description = "Supprime un groupe par ID. Accessible uniquement aux administrateurs et trackers.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Groupe supprimé avec succès"),
                    @ApiResponse(responseCode = "403", description = "Accès interdit : l'utilisateur n'a pas les droits nécessaires"),
                    @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroupe(@PathVariable Long id, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                groupeService.deleteGroupe(id);
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
