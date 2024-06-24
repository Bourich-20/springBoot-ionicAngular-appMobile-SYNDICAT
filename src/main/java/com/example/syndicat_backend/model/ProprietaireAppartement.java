package com.example.syndicat_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "proprietaire_appartement")
@Data
public class ProprietaireAppartement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_proprietaire")
    private Proprietaire proprietaire;

    @ManyToOne
    @JoinColumn(name = "id_appartement")
    private Appart appartement;

    @Column(name = "quote_part")
    private Double quotePart;

    @Column(name = "date_achat")
    private Date dateAchat;

    @Column(name = "date_vente")
    private Date dateVente;

    @Column(name = "actif")
    private boolean actif;
}