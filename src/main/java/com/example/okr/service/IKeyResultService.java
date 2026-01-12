package com.example.okr.service;

import com.example.okr.dto.keyresult.DtoKeyResult;
import com.example.okr.entities.KeyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IKeyResultService {

    public List<KeyResult> saveAll(List<DtoKeyResult> dtoKeyResults);

    public Page<KeyResult> findAll(Pageable pagination);

    //temporal
    public void deleteKeyResult();
}
