package com.example.syndicat_backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
        import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "residence")
@Data
public class Residence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_residence")
    private String nomResidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_superieur_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignorer les propriétés lors de la sérialisation
    private Admin adminSuperieur;

    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Appart> appartements;

    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL)
    private Set<Admin> adminsSecondaires; // Liste des admins secondaires
}
