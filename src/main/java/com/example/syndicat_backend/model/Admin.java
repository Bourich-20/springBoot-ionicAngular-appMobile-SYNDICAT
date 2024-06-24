package com.example.syndicat_backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "tele")
    private String tele;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    @Column(name = "actif")
    private boolean actif;

    @Column(name = "type_proprietaire", columnDefinition = "varchar(255) default 'secondaire'") // Par défaut "secondaire"
    private String typeProprietaire;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_id")
    @JsonIgnore
    private Residence residence;
    // Constructeur par défaut
    public Admin() {
        this.typeProprietaire = "secondaire"; // Définir le type par défaut à "secondaire"
    }

}