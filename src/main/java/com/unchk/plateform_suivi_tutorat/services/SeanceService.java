package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.models.Module;
import com.unchk.plateform_suivi_tutorat.models.Seance;
import com.unchk.plateform_suivi_tutorat.repositories.GroupeRepository;
import com.unchk.plateform_suivi_tutorat.repositories.ModuleRepository;
import com.unchk.plateform_suivi_tutorat.repositories.SeanceRepository;
import com.unchk.plateform_suivi_tutorat.repositories.TuteurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SeanceService {

    public static final String SEANCE_NOT_FOUND_MESSAGE = "Séance non trouvée";
    public static final String SEANCE_DELETE_ERROR_MESSAGE = "Erreur lors de la suppression de la séance";
    public static final String EMPTY_MESSAGE = "Toutes les informations doivent être fournies";
    public static final String MAX_WEEKS_REACHED_MESSAGE = "Le nombre de semaines du module est déjà atteint";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    // Récupérer toutes les séances
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    // Récupérer une séance par ID
    public Seance getSeanceById(Long id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(SEANCE_NOT_FOUND_MESSAGE));
    }

    // Créer une nouvelle séance
    public Seance createSeance(Long tuteurId, Long moduleId, Long groupeId) {
        // Validation des champs
        if (tuteurId == null || moduleId == null || groupeId == null) {
            throw new RuntimeException(EMPTY_MESSAGE);
        }

        // Créer une nouvelle séance avec les informations fournies
        Seance seance = new Seance();

        // Formater la date actuelle
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);

        // Convertir la chaîne formatée en LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(formattedDate, formatter);

        seance.setDate(dateTime); // Définir la date formatée
        seance.setTuteur(
                tuteurRepository.findById(tuteurId).orElseThrow(() -> new RuntimeException("Tuteur non trouvé")));
        seance.setModule(
                moduleRepository.findById(moduleId).orElseThrow(() -> new RuntimeException("Module non trouvé")));
        seance.setGroupe(
                groupeRepository.findById(groupeId).orElseThrow(() -> new RuntimeException("Groupe non trouvé")));

        // Initialiser heuresEffectuees et heuresNonEffectuees à 0
        seance.setHeuresEffectuees(0);
        seance.setHeuresNonEffectuees(0);

        return seanceRepository.save(seance);
    }

    // Mettre à jour l'état de la séance (effectuée ou non) et gérer les dates
    public Seance updateSeanceStatus(Long id, boolean effectuee) {
        return seanceRepository.findById(id).map(seance -> {
            Module module = seance.getModule();

            int totalHeures = seance.getHeuresEffectuees() + seance.getHeuresNonEffectuees();

            if (effectuee) {
                if (totalHeures + 2 > module.getNombreSemaines()) {
                    throw new RuntimeException(MAX_WEEKS_REACHED_MESSAGE);
                }
                seance.setHeuresEffectuees(seance.getHeuresEffectuees());
            } else {
                if (totalHeures + 2 > module.getNombreSemaines()) {
                    throw new RuntimeException(MAX_WEEKS_REACHED_MESSAGE);
                }
                seance.setHeuresNonEffectuees(seance.getHeuresNonEffectuees());
            }

            seance.setEffectuee(effectuee);
            // Formatez la date actuelle
            String formattedDate = LocalDateTime.now().format(DATE_FORMATTER);
            seance.getDates().add(formattedDate);

            return seanceRepository.save(seance);
        }).orElseThrow(() -> new RuntimeException(SEANCE_NOT_FOUND_MESSAGE));
    }

    // Supprimer une séance
    public void deleteSeance(Long id) {
        if (!seanceRepository.existsById(id)) {
            throw new RuntimeException(SEANCE_NOT_FOUND_MESSAGE);
        }
        try {
            seanceRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(SEANCE_DELETE_ERROR_MESSAGE);
        }
    }
}
