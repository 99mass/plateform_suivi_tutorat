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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String USER_DEFAULT_EMAIL = "admin@gmail.com";
    private static final String USER_DEFAULT_PASSWORD = "Passer123";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Authentifier un utilisateur", description = "Authentifie un utilisateur avec son email et mot de passe, et retourne un token JWT.", responses = {
            @ApiResponse(responseCode = "200", description = "Authentification réussie, token JWT retourné"),
            @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Vérifier si c'est le cas spécial de l'utilisateur par défaut
            if (USER_DEFAULT_EMAIL.equals(loginRequest.getEmail())
                    && USER_DEFAULT_PASSWORD.equals(loginRequest.getMotDePasse())) {
                Optional<Utilisateur> existingUser = utilisateurService.getUserByEmail(USER_DEFAULT_EMAIL);

                if (existingUser.isEmpty()) {
                    System.out.println("non default admin found");

                    // Créer l'utilisateur par défaut
                    Utilisateur defaultUser = new Utilisateur();
                    defaultUser.setEmail(USER_DEFAULT_EMAIL);
                    defaultUser.setMotDePasse(passwordEncoder.encode(USER_DEFAULT_PASSWORD));
                    defaultUser.setNom("doe");
                    defaultUser.setPrenom("admin");
                    defaultUser.setTelephone("771234567");
                    defaultUser.setRole(Utilisateur.Role.admin);

                    defaultUser = utilisateurService.createUser(defaultUser);

                    // Générer le token pour le nouvel utilisateur
                    String jwt = jwtService.generateToken(defaultUser);
                    return createAuthenticationResponse(defaultUser, jwt);
                } else {
                    // Si l'utilisateur par défaut existe déjà, renvoyer un token directement
                    System.out.println("yes default admin found");
                    String jwt = jwtService.generateToken(existingUser.get());
                    return createAuthenticationResponse(existingUser.get(), jwt);
                }
            }

            // Authentification normale pour d'autres utilisateurs
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getMotDePasse()));

            Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
            String jwt = jwtService.generateToken(utilisateur);

            return createAuthenticationResponse(utilisateur, jwt);

        } catch (Exception e) {
            // Si l'authentification échoue, renvoyer une réponse d'erreur appropriée
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Email ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private ResponseEntity<?> createAuthenticationResponse(Utilisateur utilisateur, String jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("id", utilisateur.getId());
        response.put("nom", utilisateur.getNom());
        response.put("prenom", utilisateur.getPrenom());
        response.put("email", utilisateur.getEmail());
        response.put("telephone", utilisateur.getTelephone());
        response.put("role", utilisateur.getRole());

        return ResponseEntity.ok(response);
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

    @Operation(summary = "Vérifier la validité du token", description = "Vérifie si le token JWT est valide et retourne les informations de l'utilisateur.", responses = {
            @ApiResponse(responseCode = "200", description = "Token valide, informations utilisateur retournées"),
            @ApiResponse(responseCode = "401", description = "Token invalide ou expiré")
    })
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        if (jwtService.isTokenValid(token)) {
            String email = jwtService.extractUsername(token); // extraire l'email de l'utilisateur depuis le token
            Optional<Utilisateur> utilisateurOpt = utilisateurService.getUserByEmail(email);

            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();

                // Créer une réponse avec les informations de l'utilisateur
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Token valide");
                response.put("token", token);
                response.put("id", utilisateur.getId());
                response.put("nom", utilisateur.getNom());
                response.put("prenom", utilisateur.getPrenom());
                response.put("email", utilisateur.getEmail());
                response.put("telephone", utilisateur.getTelephone());
                response.put("role", utilisateur.getRole());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Utilisateur non trouvé"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token invalide ou expiré"));
        }
    }

}
