package com.example.okr.dto.keyresult;

import com.example.okr.entities.KeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoKeyResultView {

    private String description;

    private Double percent;

    private String quarter;

    private String owner;

    private String pilar;

    private String area;

    private String objective;

    private Double objective_percent;

    public DtoKeyResultView(KeyResult keyResult)
    {
        this.description = keyResult.getDescription();
        this.percent = keyResult.getPercent();
        this.quarter = keyResult.getQuarter();
        this.owner = keyResult.getOwner();
        this.pilar = keyResult.getPilar();
        this.area = keyResult.getArea();
        this.objective = keyResult.getObjective();
        this.objective_percent = keyResult.getObjective_percent();
    }

}
