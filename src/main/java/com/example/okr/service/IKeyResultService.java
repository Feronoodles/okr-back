package com.example.okr.service;

import com.example.okr.dto.keyresult.DtoKeyResult;
import com.example.okr.entities.KeyResult;
import com.example.okr.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IKeyResultService {

    public List<KeyResult> saveAll(List<DtoKeyResult> dtoKeyResults, User user);

    public Page<KeyResult> findAll(User user, Pageable pagination);

    public Page<KeyResult> findPublic(Pageable pagination);

    public void archiveKeyResults(User user);
}
