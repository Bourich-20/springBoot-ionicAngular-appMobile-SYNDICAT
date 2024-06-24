package com.example.syndicat_backend.repository;

import com.example.syndicat_backend.model.Admin;
import com.example.syndicat_backend.model.Appart;
import com.example.syndicat_backend.model.ProprietaireAppartement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmailAndPassword(String email, String password);
    List<Admin> findByResidenceId(Long residenceId);

}
