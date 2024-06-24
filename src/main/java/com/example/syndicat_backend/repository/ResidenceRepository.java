package com.example.syndicat_backend.repository;

import com.example.syndicat_backend.model.Admin;
import com.example.syndicat_backend.model.Appart;
import com.example.syndicat_backend.model.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ResidenceRepository extends JpaRepository<Residence, Long> {
    Residence findByAdminSuperieur(Admin admin);

}
