package com.example.okr.entities;


import com.example.okr.dto.keyresult.DtoKeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "key_results")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kr_id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double percent;

    @Column(nullable = false)
    private String quarter;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String iniciativa;

    @Column(nullable = false)
    private String pilar;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String objective;

    @Column(nullable = false)
    private Double objective_percent;

    @Column(name = "user_id",nullable = false)
    private Long user_id;

    public KeyResult(DtoKeyResult dtoKeyResults)
    {
        this.description = dtoKeyResults.getDescription();
        this.percent = dtoKeyResults.getPercent();
        this.quarter = dtoKeyResults.getQuarter();
        this.owner = dtoKeyResults.getOwner();
        this.iniciativa = dtoKeyResults.getIniciativa();
        this.pilar = dtoKeyResults.getPilar();
        this.area = dtoKeyResults.getArea();
        this.objective = dtoKeyResults.getObjective();
        this.objective_percent = dtoKeyResults.getObjective_percent();
        this.user_id = 1l;


    }
    
}
