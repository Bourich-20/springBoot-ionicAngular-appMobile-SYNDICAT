package com.example.syndicat_backend.repository;

import com.example.syndicat_backend.model.Appart;
import com.example.syndicat_backend.model.ProprietaireAppartement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProprietaireAppartementRepository extends JpaRepository<ProprietaireAppartement, Long> {
    List<ProprietaireAppartement> findByAppartementId(Long appartementId);
    Optional<ProprietaireAppartement> findByProprietaireIdAndAppartementId(Long proprietaireId, Long appartementId);
    @Query("SELECT pa.appartement.id FROM ProprietaireAppartement pa WHERE pa.proprietaire.id = :proprietaireId")
    List<Long> findAppartementIdsByProprietaireId(@Param("proprietaireId") Long proprietaireId);

    @Query("SELECT pa.appartement.id FROM ProprietaireAppartement pa WHERE pa.proprietaire.id = :proprietaireId")
    Long getIdsAppartementsByProprietaireId(@Param("proprietaireId") Long proprietaireId);
    List<ProprietaireAppartement> findByProprietaireId(Long proprietaireId);

}
