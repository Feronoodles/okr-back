package com.example.okr.entities;


import com.example.okr.dto.keyresult.DtoKeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

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

    @Column(nullable = false, precision = 5, scale = 2)
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

    @Column(nullable = false,length = 500)
    private String objective;

    @Column(nullable = false, precision = 5, scale = 2)
    private Double objective_percent;

    @Column(nullable = false)
    private Boolean archived = false;

    @Column(name = "archived_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date archivedAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
    }

    public void archive() {
        this.archived = true;
        this.archivedAt = new Date();
    }
    
}
