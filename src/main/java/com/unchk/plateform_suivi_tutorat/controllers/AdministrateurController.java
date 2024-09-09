package com.unchk.plateform_suivi_tutorat.controllers;

import com.unchk.plateform_suivi_tutorat.Utiles.ErrorResponse;
import com.unchk.plateform_suivi_tutorat.models.Administrateur;
import com.unchk.plateform_suivi_tutorat.services.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AdministrateurController {

    @Autowired
    private AdministrateurService administrateurService;



    @GetMapping
    public ResponseEntity<List<Administrateur>> getAllAdmins() {
        List<Administrateur> admins = administrateurService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAdminById(@PathVariable Long id) {
        Optional<Administrateur> admin = administrateurService.getAdminById(id);

        return admin.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(AdministrateurService.ADMIN_NOT_FOUND_MESSAGE)));

    }

    @PostMapping
    public ResponseEntity<Object> createAdmin(@RequestBody Administrateur administrateur) {

        try {
            Administrateur createdAdmin = administrateurService.createAdmin(administrateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
        } catch (RuntimeException e) {

           if (e.getMessage().equals(AdministrateurService.ADMIN_NOT_FOUND_MESSAGE)
               || e.getMessage().equals(AdministrateurService.INVALID_EMAIL_MESSAGE)
               || e.getMessage().equals(AdministrateurService.INVALID_TELEPHONE_MESSAGE)
               || e.getMessage().equals(AdministrateurService.INVALID_PASSWORD_MESSAGE)
               || e.getMessage().equals(AdministrateurService.EMPTY_MESSAGE)
           ){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                       .body(new ErrorResponse(e.getMessage()));
           }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAdmin(@PathVariable Long id, @RequestBody Administrateur administrateurDetails) {
        try {
            Administrateur updatedAdmin = administrateurService.updateAdmin(id, administrateurDetails);
            return ResponseEntity.ok(updatedAdmin);
        } catch (RuntimeException e) {
            if (e.getMessage().equals(AdministrateurService.ADMIN_NOT_FOUND_MESSAGE)
                    || e.getMessage().equals(AdministrateurService.INVALID_EMAIL_MESSAGE)
                    || e.getMessage().equals(AdministrateurService.INVALID_TELEPHONE_MESSAGE)
                    || e.getMessage().equals(AdministrateurService.INVALID_PASSWORD_MESSAGE)
                    || e.getMessage().equals(AdministrateurService.EMPTY_MESSAGE)
            ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(e.getMessage()));
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable Long id) {
        try {
            administrateurService.deleteAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AdministrateurService.ADMIN_DELETE_ERROR_MESSAGE));
        }
    }
}