package com.example.okr.service;


import com.example.okr.dto.keyresult.DtoKeyResult;
import com.example.okr.entities.KeyResult;
import com.example.okr.persistence.KeyResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KeyResultImpl implements IKeyResultService{
    @Override
    public void deleteKeyResult() {
        keyResultRepository.deleteAllInBatch();
    }

    KeyResultRepository keyResultRepository;

    @Override
    public List<KeyResult> saveAll(List<DtoKeyResult> dtoKeyResults) {
        List<KeyResult> keyResults = dtoKeyResults.stream()
                .map(dto -> {
                    KeyResult keyResult = new KeyResult(dto);
                    return keyResult;
                }).toList();

        return keyResultRepository.saveAll(keyResults);
    }

    @Override
    public Page<KeyResult> findAll(Pageable pagination) {
        return (Page<KeyResult>) keyResultRepository.findAll(pagination);
    }


}
