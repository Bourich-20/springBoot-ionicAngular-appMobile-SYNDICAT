package com.example.syndicat_backend.repository;


import com.example.syndicat_backend.model.Appart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppartRepository extends JpaRepository<Appart, Long> {
    List<Appart> findByResidenceId(Long residenceId);


}
