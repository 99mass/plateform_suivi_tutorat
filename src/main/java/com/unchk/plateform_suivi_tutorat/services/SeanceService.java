package com.unchk.plateform_suivi_tutorat.services;

import com.unchk.plateform_suivi_tutorat.dtos.GroupeDTO;
import com.unchk.plateform_suivi_tutorat.dtos.ModuleDTO;
import com.unchk.plateform_suivi_tutorat.dtos.SeanceDTO;
import com.unchk.plateform_suivi_tutorat.dtos.TuteurDTO;
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
import java.util.stream.Collectors;

@Service
public class SeanceService {

    public static final String SEANCE_NOT_FOUND_MESSAGE = "Séance non trouvée";
    public static final String SEANCE_DELETE_ERROR_MESSAGE = "Erreur lors de la suppression de la séance";
    public static final String EMPTY_MESSAGE = "Toutes les informations doivent être fournies";
    public static final String MAX_WEEKS_REACHED_MESSAGE = "Le nombre de semaines du module est déjà atteint";
    public  static final String ERROR_EXISTING_SEANCE = "Une séance avec ce tuteur, module et groupe existe déjà";
    public  static final String ERROR_TUTEUR_NOT_FOUND = "Tuteur non trouvé";
    public  static final String ERROR_MODULE_NOT_FOUND = "Module non trouvé";
    public  static final String ERROR_GROUPE_NOT_FOUND = "Groupe non trouvé";


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
    public List<SeanceDTO> getAllSeances() {
        List<Seance> seances = seanceRepository.findAll();
        return seances.stream()
                .map(this::convertToSeanceDTO) // Utilisation de convertToSeanceDTO
                .collect(Collectors.toList());
    }


    // Récupérer une séance par ID
    public SeanceDTO getSeanceById(Long id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(SEANCE_NOT_FOUND_MESSAGE));
        return convertToSeanceDTO(seance); // Conversion de la séance
    }

//    Récupérer les séances par l'ID du tuteur
    public List<SeanceDTO> getSeancesByTuteurId(Long tuteurId) {
        if (!tuteurRepository.existsById(tuteurId)) {
            throw new RuntimeException(ERROR_TUTEUR_NOT_FOUND);
        }

        List<Seance> seances = seanceRepository.findByTuteurId(tuteurId);
        return seances.stream()
                .map(this::convertToSeanceDTO)
                .collect(Collectors.toList());
    }


    // Créer une nouvelle séance
    public SeanceDTO createSeance(Long tuteurId, Long moduleId, Long groupeId) {
        // Validation des champs
        if (tuteurId == null || moduleId == null || groupeId == null) {
            throw new RuntimeException(EMPTY_MESSAGE);
        }

        // Vérifier si une séance existe déjà avec le même tuteurId, moduleId, et groupeId
        if (seanceRepository.existsByTuteurIdAndModuleIdAndGroupeId(tuteurId, moduleId, groupeId)) {
            throw new RuntimeException(ERROR_EXISTING_SEANCE);
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
                tuteurRepository.findById(tuteurId).orElseThrow(() -> new RuntimeException(ERROR_TUTEUR_NOT_FOUND)));
        seance.setModule(
                moduleRepository.findById(moduleId).orElseThrow(() -> new RuntimeException(ERROR_MODULE_NOT_FOUND)));
        seance.setGroupe(
                groupeRepository.findById(groupeId).orElseThrow(() -> new RuntimeException(ERROR_GROUPE_NOT_FOUND)));

        // Initialiser heuresEffectuees et heuresNonEffectuees à
        seance.setHeuresEffectuees(0);
        seance.setHeuresNonEffectuees(0);
        seance.setEffectuee(false);

        // Sauvegarde et conversion au DTO
        Seance savedSeance = seanceRepository.save(seance);
        return convertToSeanceDTO(savedSeance); // Retourne le DTO
    }

    // Mettre à jour l'état de la séance (effectuée ou non) et gérer les dates
    public SeanceDTO updateSeanceStatus(Long id, boolean effectuee) {
        return seanceRepository.findById(id).map(seance -> {
            Module module = seance.getModule();

            int totalHeures = seance.getHeuresEffectuees() + seance.getHeuresNonEffectuees();

            if (totalHeures + 2 > module.getNombreSemaines() * 2) {
                throw new RuntimeException(MAX_WEEKS_REACHED_MESSAGE);
            }

            if (effectuee) {
                seance.setHeuresEffectuees(seance.getHeuresEffectuees()+2);
            } else {
                seance.setHeuresNonEffectuees(seance.getHeuresNonEffectuees()+2);
            }

            seance.setEffectuee(effectuee);
            // Formatez la date actuelle
            String formattedDate = LocalDateTime.now().format(DATE_FORMATTER);

            // Vérifier si la date est déjà présente
            if (!seance.getDates().contains(formattedDate)) {
                seance.addDate(formattedDate);
            }

            return convertToSeanceDTO(seanceRepository.save(seance)); // Retourne le DTO
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

    public SeanceDTO convertToSeanceDTO(Seance seance) {
        TuteurDTO tuteurDTO = new TuteurDTO();
        tuteurDTO.setId(seance.getTuteur().getId());
        tuteurDTO.setNom(seance.getTuteur().getNom());
        tuteurDTO.setPrenom(seance.getTuteur().getPrenom());
        tuteurDTO.setEmail(seance.getTuteur().getEmail());
        tuteurDTO.setTelephone(seance.getTuteur().getTelephone());
        tuteurDTO.setRole(seance.getTuteur().getRoleName());
        tuteurDTO.setModules(seance.getTuteur().getModules().stream()
                .map(module -> new ModuleDTO(module.getId(), module.getNom(), module.getNombreSemaines()))
                .collect(Collectors.toSet()));
        tuteurDTO.setGroupes(seance.getTuteur().getGroupes().stream()
                .map(groupe -> new GroupeDTO(groupe.getId(), groupe.getNom(),
                        new ModuleDTO(groupe.getModule().getId(), groupe.getModule().getNom(), groupe.getModule().getNombreSemaines())))
                .collect(Collectors.toSet()));


        SeanceDTO seanceDTO = new SeanceDTO();
        seanceDTO.setId(seance.getId());
        seanceDTO.setDate(seance.getDate());
        seanceDTO.setEffectuee(seance.isEffectuee());
        seanceDTO.setHeuresEffectuees(seance.getHeuresEffectuees());
        seanceDTO.setHeuresNonEffectuees(seance.getHeuresNonEffectuees());
        seanceDTO.setTuteur(tuteurDTO);
        seanceDTO.setModule(new ModuleDTO(seance.getModule().getId(), seance.getModule().getNom(), seance.getModule().getNombreSemaines()));
        seanceDTO.setGroupe(new GroupeDTO(
                seance.getGroupe().getId(),
                seance.getGroupe().getNom(),
                new ModuleDTO(
                        seance.getGroupe().getModule().getId(),
                        seance.getGroupe().getModule().getNom(),
                        seance.getGroupe().getModule().getNombreSemaines()
                )
        ));

        seanceDTO.setDates(seance.getDates());

        return seanceDTO;
    }

}
