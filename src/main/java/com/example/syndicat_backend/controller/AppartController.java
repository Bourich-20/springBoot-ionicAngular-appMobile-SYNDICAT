package com.example.syndicat_backend.controller;

import com.example.syndicat_backend.Exception.ResourceNotFoundException;
import com.example.syndicat_backend.model.Appart;
import com.example.syndicat_backend.model.AppartDTO;
import com.example.syndicat_backend.model.ProprietaireAppartement;
import com.example.syndicat_backend.model.Residence;
import com.example.syndicat_backend.repository.AppartRepository;
import com.example.syndicat_backend.repository.ProprietaireAppartementRepository;
import com.example.syndicat_backend.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/api/apparts")
public class AppartController {

    private final AppartRepository appartRepository;
    @Autowired

    private ResidenceRepository residenceRepository;
    @Autowired

    private ProprietaireAppartementRepository proprietaireAppartementRepository;
    @Autowired
    public AppartController(AppartRepository appartRepository) {
        this.appartRepository = appartRepository;
    }

    @GetMapping

    public List<Appart> getAllApparts() {
        return appartRepository.findAll();
    }

    @PostMapping("/ajouter")
    public ResponseEntity<Appart> addAppart(@RequestBody Appart appart) {
        Appart savedAppart = appartRepository.save(appart);
        return ResponseEntity.ok(savedAppart);
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<Appart> editAppart(@PathVariable Long id, @RequestBody Appart updatedAppart) {
        Appart existingAppart = appartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appart not found with id: " + id));
        existingAppart.setNumero(updatedAppart.getNumero());
        existingAppart.setSurfaceAppart(updatedAppart.getSurfaceAppart());
        existingAppart.setTitreFoncierAppart(updatedAppart.getTitreFoncierAppart());
        existingAppart.setNbrVoixAppart(updatedAppart.getNbrVoixAppart());

        Appart savedAppart = appartRepository.save(existingAppart);
        return ResponseEntity.ok(savedAppart);
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> deleteAppart(@PathVariable Long id) {
        appartRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/monAppart/{proprietaireId}")
    public ResponseEntity<Appart> getAppartementsByProprietaireId(@PathVariable Long proprietaireId) {
        Long idAppartement = proprietaireAppartementRepository.getIdsAppartementsByProprietaireId(proprietaireId);
        if (idAppartement != null) {
            Optional<Appart> appartement = appartRepository.findById(idAppartement);
            if (appartement.isPresent()) {
                Appart appartementWithProprietaires = appartement.get();
                // Charger explicitement les propriétaires associés à l'appartement
                appartementWithProprietaires.getProprietaires().size(); // Cela chargera les propriétaires depuis la base de données
                return new ResponseEntity<>(appartementWithProprietaires, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/monAppartProprietaire/{proprietaireId}")
    public ResponseEntity<List<ProprietaireAppartement>> getProprietaireAppartement(@PathVariable Long proprietaireId) {
        List<ProprietaireAppartement> proprietaireAppartements = proprietaireAppartementRepository.findByProprietaireId(proprietaireId);
        if (!proprietaireAppartements.isEmpty()) {
            return ResponseEntity.ok(proprietaireAppartements);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("get")
    public ResponseEntity<List<Appart>> getAllAppartementsByResidenceId(@PathVariable Long residenceId) {
        List<Appart> appartements = appartRepository.findByResidenceId(residenceId);
        return new ResponseEntity<>(appartements, HttpStatus.OK);
    }

    @GetMapping("/residence/{residenceId}")
    public ResponseEntity<List<Appart>> getAppartementsByResidenceId(@PathVariable Long residenceId) {
        List<Appart> appartements = appartRepository.findByResidenceId(residenceId);
        if (appartements != null) {
            return new ResponseEntity<>(appartements, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }

    @PostMapping("/addAppat/{residenceId}")
    public ResponseEntity<Appart> addAppartement(@RequestBody AppartDTO appartDTO, @PathVariable Long residenceId) {
        // Convertir AppartDTO en Appart
        Appart appart = new Appart();
        appart.setId(appartDTO.getId());
        appart.setNumero(appartDTO.getNumero());
        appart.setSurfaceAppart(appartDTO.getSurfaceAppart());
        appart.setTitreFoncierAppart(appartDTO.getTitreFoncierAppart());
        appart.setNbrVoixAppart(appartDTO.getNbrVoixAppart());

        // Récupérer la résidence à partir de residenceId et l'associer à l'appartement
        Optional<Residence> residence = residenceRepository.findById(residenceId);
        if (residence.isPresent()) {
            appart.setResidence(residence.get());
            Appart savedAppartement = appartRepository.save(appart);
            return new ResponseEntity<>(savedAppartement, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Appart> editAppartement(@PathVariable Long id, @RequestBody Appart appart) {
        Optional<Appart> existingAppart = appartRepository.findById(id);
        if (existingAppart.isPresent()) {
            Appart updatedAppart = existingAppart.get();
            updatedAppart.setNumero(appart.getNumero());
            updatedAppart.setSurfaceAppart(appart.getSurfaceAppart());
            updatedAppart.setTitreFoncierAppart(appart.getTitreFoncierAppart());
            updatedAppart.setNbrVoixAppart(appart.getNbrVoixAppart());
            Appart savedAppartement = appartRepository.save(updatedAppart);
            return new ResponseEntity<>(savedAppartement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delet/{id}")
    public ResponseEntity<Void> deleteAppartement(@PathVariable Long id) {
        Optional<Appart> existingAppart = appartRepository.findById(id);
        if (existingAppart.isPresent()) {
            appartRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
