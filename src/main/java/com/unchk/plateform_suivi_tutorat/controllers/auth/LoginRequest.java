package com.unchk.plateform_suivi_tutorat.controllers.auth;

public class LoginRequest {
    private String email;
    private String motDePasse;

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String password) {
        this.motDePasse = password;
    }
}
