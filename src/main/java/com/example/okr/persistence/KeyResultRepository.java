package com.example.okr.persistence;

import com.example.okr.entities.KeyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyResultRepository extends JpaRepository<KeyResult,Long> {

}
