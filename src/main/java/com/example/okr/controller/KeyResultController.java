package com.example.okr.controller;


import com.example.okr.dto.keyresult.DtoKeyResult;
import com.example.okr.dto.keyresult.DtoKeyResultView;

import com.example.okr.entities.KeyResult;

import com.example.okr.service.IKeyResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/key_result")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*") // Permite peticiones de cualquier origen
public class KeyResultController {

    private IKeyResultService iKeyResultService;

    @Operation(summary = "Añadir Key Results", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Add", content = @Content(mediaType = "application/json",schema = @Schema(implementation = DtoKeyResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PostMapping
    public ResponseEntity addKeyResults(@RequestBody @Valid List<DtoKeyResult> dtoKeyResultList, UriComponentsBuilder uriComponentsBuilder) {
        List<KeyResult> keyResults = iKeyResultService.saveAll(dtoKeyResultList);
        List<DtoKeyResultView> keyResultViews = keyResults.stream().map(
                dto -> {
                    DtoKeyResultView dtoKeyResultView = new DtoKeyResultView(dto);
                    return dtoKeyResultView;
                }
        ).toList();
        URI url = uriComponentsBuilder.path("/key_result").buildAndExpand().toUri();
        return ResponseEntity.created(url).body(keyResultViews);
    }

    @Operation(summary = "Ver Key Results", responses = {
            @ApiResponse(responseCode = "200", description = "Successful search", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Pageable.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @GetMapping("/view_key_results")
    public ResponseEntity<Page<DtoKeyResultView>> viewKeyResults(@PageableDefault(size = 10) Pageable pagination) {

        return ResponseEntity.ok(iKeyResultService.findAll(pagination).map(DtoKeyResultView::new));
    }

    @Operation(summary = "Eliminar key results", responses = {
            @ApiResponse(responseCode = "204", description = "Successful delete"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @DeleteMapping
    public ResponseEntity deleteKeyResults()
    {
        iKeyResultService.deleteKeyResult();
        return ResponseEntity.noContent().build();
    }


}
