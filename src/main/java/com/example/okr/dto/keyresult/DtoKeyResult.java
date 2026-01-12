package com.example.okr.dto.keyresult;

import com.example.okr.entities.KeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoKeyResult {

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double percent;

    @NotBlank
    private String quarter;

    @NotBlank
    private String owner;

    @NotBlank
    private String pilar;

    @NotBlank
    private String area;

    @NotBlank
    private String objective;

    @NotNull
    @Positive
    private Double objective_percent;



}
