package com.unchk.plateform_suivi_tutorat.Utiles;
import java.util.regex.Pattern;

public class Helper {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String TELEPHONE_REGEX = "^(\\+221)?[78]\\d{8}$";
    private static final int MIN_PASSWORD_LENGTH = 6;

    public boolean isValidEmail(String email) {
        return email == null || !Pattern.matches(EMAIL_REGEX, email);
    }

    public boolean isValidTelephone(String telephone) {
        return telephone == null || !Pattern.matches(TELEPHONE_REGEX, telephone);
    }

    public boolean isValidPassword(String motDePasse) {
        return motDePasse == null || motDePasse.length() < MIN_PASSWORD_LENGTH;
    }

}
