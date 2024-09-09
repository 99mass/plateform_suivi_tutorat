package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import com.unchk.plateform_suivi_tutorat.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UtilisateurController {

    public static final String USER_ROLE_ADMIN = Utilisateur.Role.admin.name();
    public static final String USER_ROLE_TRACKER = Utilisateur.Role.tracker.name();
    public static final String USER_ROLE_TUTEUR = Utilisateur.Role.tuteur.name();
    public static final String USER_FORBIDDEN_Message = "Vous n'avez pas les droits pour créer cet utilisateur";


    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> users = utilisateurService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> user = utilisateurService.getUserById(id);
        return user.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(UtilisateurService.USER_NOT_FOUND_MESSAGE)));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Object> createUser(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur createdUser = utilisateurService.createUser(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return handleCreateOrUpdateError(e);
        }
    }

    @PostMapping("/create-tracker")
    public ResponseEntity<Object> createTracker(@RequestBody Utilisateur utilisateur, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (role.equals(USER_ROLE_ADMIN)) {
                Utilisateur createdUser = utilisateurService.createUser(utilisateur);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_Message));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/create-tuteur")
    public ResponseEntity<Object> createTuteur(@RequestBody Utilisateur utilisateur, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (role.equals(USER_ROLE_ADMIN) || role.equals(USER_ROLE_TRACKER)) {
                Utilisateur createdUser = utilisateurService.createUser(utilisateur);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_Message));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails) {
        try {
            Utilisateur updatedUser = utilisateurService.updateUser(id, utilisateurDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return handleCreateOrUpdateError(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            utilisateurService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(UtilisateurService.USER_DELETE_ERROR_MESSAGE));
        }
    }

    private ResponseEntity<Object> handleCreateOrUpdateError(RuntimeException e) {
        if (e.getMessage().equals(UtilisateurService.USER_NOT_FOUND_MESSAGE)
                || e.getMessage().equals(UtilisateurService.INVALID_EMAIL_MESSAGE)
                || e.getMessage().equals(UtilisateurService.INVALID_TELEPHONE_MESSAGE)
                || e.getMessage().equals(UtilisateurService.INVALID_PASSWORD_MESSAGE)
                || e.getMessage().equals(UtilisateurService.EMPTY_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
