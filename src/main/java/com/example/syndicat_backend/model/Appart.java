package com.example.syndicat_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;
@Entity
@Table(name = "appart")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "surface_appart")
    private Double surfaceAppart;

    @Column(name = "titre_foncier_appart")
    private String titreFoncierAppart;

    @Column(name = "nbr_voix_appart")
    private Integer nbrVoixAppart;

    @OneToMany(mappedBy = "appartement", cascade = CascadeType.ALL)
    private Set<ProprietaireAppartement> proprietaires;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "proprietaires"}) // Ignorer les propriétés lors de la sérialisation
    private Residence residence;
}
