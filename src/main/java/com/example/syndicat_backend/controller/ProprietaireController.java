package com.example.syndicat_backend.controller;

import com.example.syndicat_backend.model.*;
import com.example.syndicat_backend.repository.AppartRepository;
import com.example.syndicat_backend.repository.ProprietaireAppartementRepository;
import com.example.syndicat_backend.repository.ProprietaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/api/proprietaires")
public class ProprietaireController {
    @Autowired

    private  ProprietaireAppartementRepository proprietaireAppartementRepository;
    @Autowired
    private ProprietaireRepository proprietaireRepository;
    @Autowired
    private AppartRepository appartRepository;

    @Autowired
    private AppartRepository appartementRepository;
    @GetMapping("/{proprietaireId}")
    public ResponseEntity<?> getProprietaireById(@PathVariable Long proprietaireId) {
        try {
            Proprietaire proprietaire = proprietaireRepository.findById(proprietaireId).orElse(null);

            if (proprietaire != null) {
                return ResponseEntity.ok(proprietaire);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération du propriétaire.");
        }
    }
    @GetMapping("/by-appartement/{appartementId}")
    public ResponseEntity<?> getProprietairesByAppartement(@PathVariable Long appartementId) {
        try {

            // Recherchez les relations ProprietaireAppartement par ID d'appartement
            List<ProprietaireAppartement> relations = proprietaireAppartementRepository.findByAppartementId(appartementId);

            // Vérifiez s'il y a des relations
            List<Proprietaire> proprietaires = new ArrayList<>();
            for (ProprietaireAppartement relation : relations) {
                proprietaires.add(relation.getProprietaire());
            }

            return ResponseEntity.ok(proprietaires);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<Proprietaire>()); // Renvoyer une liste vide en cas d'erreur
        }
    }
    @GetMapping("/{proprietaireId}/appartement/{appartementId}")
    public ResponseEntity<?> getProprietaireAppartement(@PathVariable Long proprietaireId, @PathVariable Long appartementId) {
        try {
            Optional<ProprietaireAppartement> relationOptional = proprietaireAppartementRepository.findByProprietaireIdAndAppartementId(proprietaireId, appartementId);

            if (relationOptional.isPresent()) {
                ProprietaireAppartement relation = relationOptional.get();
                return ResponseEntity.ok(relation);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération de la relation ProprietaireAppartement.");
        }
    }
    @PutMapping("/updateProprietaire/{id}")
    public ResponseEntity<Proprietaire> updateProprietaire(@PathVariable Long id, @RequestBody Proprietaire proprietaireDetails) {
        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
        System.out.println("proprietaireDetails"+proprietaireDetails);
        // Vérifie si le propriétaire existe dans la base de données
        if (optionalProprietaire.isPresent()) {
            Proprietaire proprietaire = optionalProprietaire.get();

            // Mettre à jour les informations du propriétaire
            proprietaire.setNom(proprietaireDetails.getNom());
            proprietaire.setPrenom(proprietaireDetails.getPrenom());
            proprietaire.setCin(proprietaireDetails.getCin());
            proprietaire.setTel(proprietaireDetails.getTel());
            proprietaire.setLogin(proprietaireDetails.getLogin());
            proprietaire.setPassword(proprietaireDetails.getPassword());

            // Enregistrer les modifications dans la base de données
            Proprietaire updatedProprietaire = proprietaireRepository.save(proprietaire);
            return new ResponseEntity<>(updatedProprietaire, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/proprietaire/{proprietaireId}")
    public ResponseEntity<?> getResidenceByProprietaireId(@PathVariable Long proprietaireId) {
        try {
            Long idsAppartements = proprietaireAppartementRepository.getIdsAppartementsByProprietaireId(proprietaireId);
            Optional<Appart> appartOptional = appartRepository.findById(idsAppartements);

            if (appartOptional.isPresent()) {
                Appart appart = appartOptional.get();
                Residence residence = appart.getResidence();

                ResidenceDTO residenceDTO = new ResidenceDTO();
                residenceDTO.setId(residence.getId());
                residenceDTO.setNomResidence(residence.getNomResidence());
                // Définissez les autres propriétés nécessaires

                return ResponseEntity.ok(residenceDTO);
            } else {
                return ResponseEntity.badRequest().body("Aucune résidence trouvée pour ce propriétaire.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération de la résidence du propriétaire.");
        }
    }



    @PutMapping("/update/{proprietaireId}")
    public ResponseEntity<?> editProprietaire(@PathVariable Long proprietaireId, @RequestBody Proprietaire proprietaireData) {
        try {
            System.out.println("proprietaireId   " + proprietaireId);
            System.out.println("proprietaireData   " + proprietaireData);

            Optional<Proprietaire> existingProprietaire = proprietaireRepository.findById(proprietaireId);

            if (existingProprietaire.isPresent()) {
                Proprietaire updatedProprietaire = existingProprietaire.get();

                // Utilisez les méthodes appropriées pour mettre à jour chaque champ
                updatedProprietaire.setNom(proprietaireData.getNom());
                updatedProprietaire.setPrenom(proprietaireData.getPrenom());
                updatedProprietaire.setCin(proprietaireData.getCin());
                updatedProprietaire.setTel(proprietaireData.getTel());
                updatedProprietaire.setEmail(proprietaireData.getEmail());
                updatedProprietaire.setLogin(proprietaireData.getLogin());
                updatedProprietaire.setPassword(proprietaireData.getPassword());

                // Mettez à jour d'autres champs selon vos besoins
                proprietaireRepository.save(updatedProprietaire);

                return ResponseEntity.ok(Map.of("message", "Propriétaire mis à jour avec succès."));
            } else {
                return ResponseEntity.badRequest().body("Propriétaire non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du propriétaire.");
        }
    }


    @PutMapping("/{proprietaireId}/edit-appartement/{appartementId}")
    public ResponseEntity<?> editProprietaireAppartement(
            @PathVariable Long proprietaireId,
            @PathVariable Long appartementId,
            @RequestBody ProprietaireAppartement proprietaireAppartementData) {

        try {


            // Récupérez l'objet ProprietaireAppartement depuis la base de données
            ProprietaireAppartement existingRelation = proprietaireAppartementRepository
                    .findByProprietaireIdAndAppartementId(proprietaireId, appartementId)
                    .orElse(new ProprietaireAppartement());

            // Mettez à jour les champs nécessaire s avec les nouvelles données
            existingRelation.setQuotePart(proprietaireAppartementData.getQuotePart());
            existingRelation.setDateAchat(proprietaireAppartementData.getDateAchat());
            existingRelation.setDateVente(proprietaireAppartementData.getDateVente());
            existingRelation.setActif(proprietaireAppartementData.isActif());

            // Sauvegardez les modifications dans la base de données
            proprietaireAppartementRepository.save(existingRelation);

            return ResponseEntity.ok(Map.of("message", "Relation propriétaire-appartement mise à jour avec succès."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la relation propriétaire-appartement.");
        }
    }
    @PostMapping("/ajouter/{appartementId}")
    public ResponseEntity<?> addNewProprietaire(@RequestBody Proprietaire proprietaire, @PathVariable Long appartementId) {
        try {
            // Sauvegardez d'abord le propriétaire
            // ans la base de données
            Proprietaire savedProprietaire = proprietaireRepository.save(proprietaire);

            // Recherchez l'appartement par son ID
            Optional<Appart> appartOptional = appartRepository.findById(appartementId);
            if (appartOptional.isPresent()) {
                Appart appartement = appartOptional.get();

                // Associez le propriétaire à l'appartement
                ProprietaireAppartement proprietaireAppartementData = new ProprietaireAppartement();
                proprietaireAppartementData.setProprietaire(savedProprietaire);
                proprietaireAppartementData.setAppartement(appartement);

                // Enregistrez la relation avec l'appartement
                ProprietaireAppartement savedRelation = proprietaireAppartementRepository.save(proprietaireAppartementData);

                return ResponseEntity.ok(savedRelation);
            } else {
                return ResponseEntity.badRequest().body("L'appartement avec l'ID fourni n'a pas été trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout du propriétaire");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Proprietaire> loginAsProprietaire(@RequestBody Proprietaire proprietaire) {
        Proprietaire existingProprietaire = proprietaireRepository.findByEmailAndPassword(proprietaire.getEmail(), proprietaire.getPassword());
        System.out.println("existingProprietaire"+existingProprietaire);
        if (existingProprietaire != null) {
            return ResponseEntity.ok(existingProprietaire);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
