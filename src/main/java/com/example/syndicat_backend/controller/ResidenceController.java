package com.example.syndicat_backend.controller;

import com.example.syndicat_backend.model.Admin;
import com.example.syndicat_backend.model.Residence;
import com.example.syndicat_backend.repository.AdminRepository;
import com.example.syndicat_backend.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/api/residence")
public class ResidenceController {

    @Autowired
    private ResidenceRepository residenceRepository;
    @PostMapping("/ajouter")
    public ResponseEntity<Residence> createAdmin(@RequestBody Residence residence) {
        try {
            System.out.println(residence);
            Residence nouvelResidence = residenceRepository.save(residence);
            return new ResponseEntity<>(nouvelResidence, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
