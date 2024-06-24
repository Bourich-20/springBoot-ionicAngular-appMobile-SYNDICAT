package com.example.syndicat_backend.controller;

import com.example.syndicat_backend.Exception.ResourceNotFoundException;
import com.example.syndicat_backend.model.Admin;
import com.example.syndicat_backend.model.Residence;
import com.example.syndicat_backend.repository.AdminRepository;
import com.example.syndicat_backend.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ResidenceRepository residenceRepository;

    @PostMapping("/ajouter")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        try {
            Admin nouvelAdmin = adminRepository.save(admin);
            return new ResponseEntity<>(nouvelAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addAdmin/{residenceId}")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin, @PathVariable Long residenceId) {
        try {
            Residence residence = residenceRepository.findById(residenceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Residence not found with id: " + residenceId));

            admin.setResidence(residence);

            Admin nouvelAdmin = adminRepository.save(admin);

            return new ResponseEntity<>(nouvelAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/admins/{residenceId}")
    public ResponseEntity<List<Admin>> getAdminsByResidenceId(@PathVariable Long residenceId) {
        List<Admin> admins = new ArrayList<>();
        admins = adminRepository.findByResidenceId(residenceId);

        Optional<Residence> optionalResidence = residenceRepository.findById(residenceId);
        if (optionalResidence.isPresent()) {
            Residence residence = optionalResidence.get();

            if (residence.getAdminSuperieur() != null) {
                admins.add(residence.getAdminSuperieur());
            }

            admins.addAll(residence.getAdminsSecondaires());
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(admins);
    }
    @PutMapping("/update/{adminId}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long adminId, @RequestBody Admin updatedAdminData) {
        try {
            Admin adminToUpdate = adminRepository.findById(adminId)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

            adminToUpdate.setNom(updatedAdminData.getNom());
            adminToUpdate.setPrenom(updatedAdminData.getPrenom());
            adminToUpdate.setTele(updatedAdminData.getTele());
            adminToUpdate.setEmail(updatedAdminData.getEmail());
            adminToUpdate.setPassword(updatedAdminData.getPassword());
            adminToUpdate.setActif(updatedAdminData.isActif());

            Admin updatedAdmin = adminRepository.save(adminToUpdate);

            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            e.printStackTrace(); // Afficher la stack trace de l'exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Retourner une réponse avec un code d'erreur 400
        }
    }
    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        adminRepository.deleteById(adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login/{email}/{password}")
    public ResponseEntity<Long> loginAsAdmin(@PathVariable String email, @PathVariable String password) {
        System.out.println("email"+email);
        System.out.println("password"+password);

        Admin existingAdmin = adminRepository.findByEmailAndPassword(email, password);
        System.out.println("existingAdmin.getId()"+existingAdmin.getId());
        if (existingAdmin != null) {
            // Return the ID of the found admin
            return ResponseEntity.ok(existingAdmin.getId());
        } else {
            // No admin found, return a 401 (Unauthorized) response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/residence/{adminId}")
    public ResponseEntity<?> getResidenceByAdminId(@PathVariable Long adminId) {
        try {
              System.out.println("adminId residence"+adminId);
              Admin admin = adminRepository.findById(adminId).orElse(null);
              System.out.println("admin"+admin);
            if (admin != null) {
                // Vérifiez si l'administrateur a une résidence associée
                if (admin.getResidence() != null) {
                    System.out.println("admin.getResidence()"+admin.getResidence());
                    return ResponseEntity.ok(admin.getResidence());
                } else {
                    // Recherchez la résidence par l'administrateur supérieur s'il n'y a pas de résidence associée à l'administrateur
                    Residence residence = residenceRepository.findByAdminSuperieur(admin);
                    System.out.println("residence"+residence);

                    if (residence != null) {
                        return ResponseEntity.ok(residence);
                    } else {
                        return ResponseEntity.badRequest().body("Résidence non trouvée pour l'administrateur.");
                    }
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération de la résidence.");
        }

    }
    @GetMapping("/{adminId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long adminId) {
        try {
            System.out.println("adminId"+adminId);
            Admin admin = adminRepository.findById(adminId).orElse(null);

            if (admin != null) {
                System.out.println("admin"+admin);

                return ResponseEntity.ok(admin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
