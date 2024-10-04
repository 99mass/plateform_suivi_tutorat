package com.unchk.plateform_suivi_tutorat.Utiles;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Helper {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private static final String TELEPHONE_REGEX = "^\\d{9}$";
    private static final int MIN_PASSWORD_LENGTH = 6;

    public boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public boolean isValidTelephone(String telephone) {
        return telephone != null && Pattern.matches(TELEPHONE_REGEX, telephone);
    }

    public boolean isValidPassword(String motDePasse) {
        return motDePasse != null && motDePasse.length() >= MIN_PASSWORD_LENGTH;
    }

    public boolean isValidDate(LocalDateTime date) {
        // Exemple de validation simple : vérifier si la date n'est pas dans le passé
        return date.isAfter(LocalDateTime.now());
    }

}
