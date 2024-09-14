package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.models.Groupe;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.GroupeService;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
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

    @GetMapping
    public ResponseEntity<List<Groupe>> getAllGroupes() {
        List<Groupe> groupes = groupeService.getAllGroupes();
        return new ResponseEntity<>(groupes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Groupe> getGroupeById(@PathVariable Long id) {
        Optional<Groupe> groupe = groupeService.getGroupeById(id);
        return groupe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

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
