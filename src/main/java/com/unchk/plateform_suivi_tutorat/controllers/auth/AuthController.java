package com.unchk.plateform_suivi_tutorat.controllers.auth;

import com.unchk.plateform_suivi_tutorat.models.Utilisateur;
import com.unchk.plateform_suivi_tutorat.services.JwtService;
import com.unchk.plateform_suivi_tutorat.services.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UtilisateurService utilisateurService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UtilisateurService utilisateurService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.utilisateurService = utilisateurService;
    }

    @Operation(summary = "Authentifier un utilisateur", description = "Authentifie un utilisateur avec son email et mot de passe, et retourne un token JWT.", responses = {
            @ApiResponse(responseCode = "200", description = "Authentification réussie, token JWT retourné"),
            @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getMotDePasse()));

            Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
            String jwt = jwtService.generateToken(utilisateur);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("id", utilisateur.getId());
            response.put("nom", utilisateur.getNom());
            response.put("prenom", utilisateur.getPrenom());
            response.put("email", utilisateur.getEmail());
            response.put("telephone", utilisateur.getTelephone());
            response.put("role", utilisateur.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Si l'authentification échoue, renvoyer une réponse d'erreur appropriée
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Email ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @Operation(summary = "Vérifier si un utilisateur existe", description = "Vérifie si un utilisateur avec l'email spécifié existe.", responses = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/check-user")
    public ResponseEntity<?> checkIfUserExists(@RequestParam String email) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.getUserByEmail(email);

        if (utilisateurOpt.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Utilisateur trouvé");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Vérifier la validité du token", description = "Vérifie si le token JWT est valide.", responses = {
            @ApiResponse(responseCode = "200", description = "Token valide"),
            @ApiResponse(responseCode = "401", description = "Token invalide ou expiré")
    })
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        boolean isValid = jwtService.isTokenValid(token);
        if (isValid) {
            return ResponseEntity.ok(Map.of("message", "Token valide"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token invalide ou expiré"));
        }
    }
}
