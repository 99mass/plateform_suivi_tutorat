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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UtilisateurController {

    public static final String DEFAULT_PASSWORD = "Passer123";
    public static final String USER_ROLE_ADMIN = Utilisateur.Role.admin.name();
    public static final String USER_ROLE_TRACKER = Utilisateur.Role.tracker.name();
    public static final String USER_ROLE_TUTEUR = Utilisateur.Role.tuteur.name();
    public static final String USER_FORBIDDEN_MESSAGE = "Vous n'avez pas les droits pour accéder à cette ressource";
    public static final String USER_ACCES_INTERDIT = "Accès interdit pour ce compte";

    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }

    // Route pour récupérer tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers(HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role)) {
                List<Utilisateur> users = utilisateurService.getAllUsers();
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour récupérer uniquement les administrateurs
    @GetMapping("/admins")
    public ResponseEntity<Object> getAdmins(HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role)) {
                List<Utilisateur> admins = utilisateurService.getAllUsers().stream()
                        .filter(user -> USER_ROLE_ADMIN.equals(user.getRole().name()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(admins);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour récupérer uniquement les trackers
    @GetMapping("/trackers")
    public ResponseEntity<Object> getTrackers(HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role)) {
                List<Utilisateur> trackers = utilisateurService.getAllUsers().stream()
                        .filter(user -> USER_ROLE_TRACKER.equals(user.getRole().name()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(trackers);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour récupérer uniquement les tuteurs
    @GetMapping("/tuteurs")
    public ResponseEntity<Object> getTuteurs(HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                List<Utilisateur> tuteurs = utilisateurService.getAllUsers().stream()
                        .filter(user -> USER_ROLE_TUTEUR.equals(user.getRole().name()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(tuteurs);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            String email = jwtService.extractUsernameFromToken(request);

            // Récupère l'utilisateur à partir de l'ID
            Optional<Utilisateur> user = utilisateurService.getUserById(id);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(UtilisateurService.USER_NOT_FOUND_MESSAGE));
            }

            Utilisateur requestedUser = user.get();
            String requestedUserRole = requestedUser.getRole().name();

            // Vérifie si l'utilisateur connecté est l'admin
            if (USER_ROLE_ADMIN.equals(role)) {
                return ResponseEntity.ok(requestedUser);
            }

            // Vérifie si l'utilisateur connecté est un tracker
            if (USER_ROLE_TRACKER.equals(role)) {
                if (email.equals(requestedUser.getEmail()) || USER_ROLE_TUTEUR.equals(requestedUserRole)) {
                    return ResponseEntity.ok(requestedUser);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse(USER_ACCES_INTERDIT));
                }
            }

            // Vérifie si l'utilisateur connecté est un tuteur
            if (USER_ROLE_TUTEUR.equals(role)) {
                if (email.equals(requestedUser.getEmail())) {
                    return ResponseEntity.ok(requestedUser);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse(USER_ACCES_INTERDIT));
                }
            }

            // Cas par défaut si aucun des rôles ne correspond
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(USER_ACCES_INTERDIT));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }


    @PostMapping("/create-admin")
    public ResponseEntity<Object> createUser(@RequestBody Utilisateur utilisateur) {
        try {
            utilisateur.setMotDePasse(DEFAULT_PASSWORD);
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

                utilisateur.setMotDePasse(DEFAULT_PASSWORD);
                Utilisateur createdUser = utilisateurService.createUser(utilisateur);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
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
                utilisateur.setMotDePasse(DEFAULT_PASSWORD);
                Utilisateur createdUser = utilisateurService.createUser(utilisateur);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/update-admin/{id}")
    public ResponseEntity<Object> updateAdmin(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role)) {
                Utilisateur updatedUser = utilisateurService.updateUser(id, utilisateurDetails);
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return handleCreateOrUpdateError(e);
        }
    }

    @PutMapping("/update-tracker/{id}")
    public ResponseEntity<Object> updateTracker(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            String email = jwtService.extractUsernameFromToken(request);

            // Récupère l'utilisateur à modifier à partir de l'ID
            Optional<Utilisateur> userToUpadate = utilisateurService.getUserById(id);

            if (userToUpadate.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(UtilisateurService.USER_NOT_FOUND_MESSAGE));
            }

            Utilisateur targetUser = userToUpadate.get();

            if (email.equals(targetUser.getEmail()) ||  USER_ROLE_ADMIN.equals(role)) {
                Utilisateur updatedUser = utilisateurService.updateUser(id, utilisateurDetails);
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return handleCreateOrUpdateError(e);
        }
    }

    @PutMapping("/update-tuteur/{id}")
    public ResponseEntity<Object> updateTuteur(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            String email = jwtService.extractUsernameFromToken(request);

            // Récupère l'utilisateur à modifier à partir de l'ID
            Optional<Utilisateur> userToUpadate = utilisateurService.getUserById(id);

            if (userToUpadate.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(UtilisateurService.USER_NOT_FOUND_MESSAGE));
            }

            Utilisateur targetUser = userToUpadate.get();

            if (email.equals(targetUser.getEmail()) || USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {

                Utilisateur updatedUser = utilisateurService.updateUser(id, utilisateurDetails);
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return handleCreateOrUpdateError(e);
        }
    }

    @DeleteMapping("/detete-user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);
            String email = jwtService.extractUsernameFromToken(request);

            // Récupère l'utilisateur à supprimer à partir de l'ID
            Optional<Utilisateur> userToDelete = utilisateurService.getUserById(id);

            if (userToDelete.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(UtilisateurService.USER_NOT_FOUND_MESSAGE));
            }

            Utilisateur targetUser = userToDelete.get();
            String targetUserRole = targetUser.getRole().name();

            // Vérifie si l'utilisateur est admin
            if (USER_ROLE_ADMIN.equals(role)) {
                utilisateurService.deleteUser(id);
                return ResponseEntity.noContent().build();
            }

            // Vérifie si l'utilisateur est tracker
            if (USER_ROLE_TRACKER.equals(role)) {
                // Un tracker ne peut pas supprimer son propre compte ou d'autres trackers/admins
                if (email.equals(targetUser.getEmail()) || USER_ROLE_TRACKER.equals(targetUserRole) || USER_ROLE_ADMIN.equals(targetUserRole)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse(USER_ACCES_INTERDIT));
                }
                // Le tracker peut supprimer un tuteur
                if (USER_ROLE_TUTEUR.equals(targetUserRole)) {
                    utilisateurService.deleteUser(id);
                    return ResponseEntity.noContent().build();
                }
            }

            // Les tuteurs ne peuvent pas supprimer de compte
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(USER_ACCES_INTERDIT));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
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
