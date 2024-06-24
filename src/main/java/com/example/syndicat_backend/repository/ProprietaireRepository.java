package com.example.syndicat_backend.repository;

import com.example.syndicat_backend.model.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {
    List<Proprietaire> findByAppartements_Id(Long appartementId);
    Proprietaire findByEmailAndPassword(String email, String password);

}
