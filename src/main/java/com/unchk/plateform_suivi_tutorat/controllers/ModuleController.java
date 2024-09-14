package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.models.Module;
import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import com.unchk.plateform_suivi_tutorat.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    public static final String USER_ROLE_ADMIN = Utilisateur.Role.admin.name();
    public static final String USER_ROLE_TRACKER = Utilisateur.Role.tracker.name();
    public static final String USER_ROLE_TUTEUR = Utilisateur.Role.tuteur.name();
    public static final String USER_FORBIDDEN_MESSAGE = "Vous n'avez pas les droits pour accéder à cette ressource";

    private final ModuleService moduleService;
    private final JwtService jwtService;

    @Autowired
    public ModuleController(ModuleService moduleService, JwtService jwtService) {
        this.moduleService = moduleService;
        this.jwtService = jwtService;
    }

    // Route pour récupérer tous les modules
    @GetMapping("/all")
    public ResponseEntity<Object> getAllModules(HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role) || USER_ROLE_TUTEUR.equals(role)) {
                List<Module> modules = moduleService.getAllModules();
                return ResponseEntity.ok(modules);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour récupérer un module par ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getModuleById(@PathVariable Long id, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            Optional<Module> module = moduleService.getModuleById(id);

            if (module.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(ModuleService.ERROR_NOT_FOUND));
            }

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role) || USER_ROLE_TUTEUR.equals(role)) {
                return ResponseEntity.ok(module.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour créer un module
    @PostMapping("/create")
    public ResponseEntity<Object> createModule(@RequestBody Module module, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                Module createdModule = moduleService.createModule(module);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdModule);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour mettre à jour un module
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateModule(@PathVariable Long id, @RequestBody Module moduleDetails, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                Module updatedModule = moduleService.updateModule(id, moduleDetails);
                return ResponseEntity.ok(updatedModule);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(USER_FORBIDDEN_MESSAGE));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Route pour supprimer un module
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModule(@PathVariable Long id, HttpServletRequest request) {
        try {
            String role = jwtService.extractRoleFromToken(request);

            if (USER_ROLE_ADMIN.equals(role) || USER_ROLE_TRACKER.equals(role)) {
                moduleService.deleteModule(id);
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
