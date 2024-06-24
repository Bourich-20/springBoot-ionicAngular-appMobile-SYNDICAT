package com.example.syndicat_backend.model;

import lombok.Data;

@Data
public class AppartDTO {
    private Long id;
    private String numero;
    private Double surfaceAppart;
    private String titreFoncierAppart;
    private Integer nbrVoixAppart;
}
