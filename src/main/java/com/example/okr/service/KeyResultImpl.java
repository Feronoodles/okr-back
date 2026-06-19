package com.example.okr.service;

import com.example.okr.dto.keyresult.DtoKeyResult;
import com.example.okr.entities.KeyResult;
import com.example.okr.entities.User;
import com.example.okr.persistence.KeyResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class KeyResultImpl implements IKeyResultService{
    KeyResultRepository keyResultRepository;

    @Override
    public List<KeyResult> saveAll(List<DtoKeyResult> dtoKeyResults, User user) {
        List<KeyResult> keyResults = dtoKeyResults.stream()
                .map(dto -> {
                    KeyResult keyResult = new KeyResult(dto);
                    keyResult.setUser(user);
                    return keyResult;
                }).toList();

        return keyResultRepository.saveAll(keyResults);
    }

    @Override
    public Page<KeyResult> findAll(User user, Pageable pagination) {
        return keyResultRepository.findByUserAndArchivedFalse(user, pagination);
    }

    @Override
    public Page<KeyResult> findPublic(Pageable pagination) {
        return keyResultRepository.findByArchivedFalse(pagination);
    }

    @Override
    @Transactional
    public void archiveKeyResults(User user) {
        keyResultRepository.findAllByUserAndArchivedFalse(user)
                .forEach(KeyResult::archive);
    }

}
