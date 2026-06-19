package com.example.okr.persistence;

import com.example.okr.entities.KeyResult;
import com.example.okr.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyResultRepository extends JpaRepository<KeyResult,Long> {
    Page<KeyResult> findByUserAndArchivedFalse(User user, Pageable pageable);

    java.util.List<KeyResult> findAllByUserAndArchivedFalse(User user);

    Page<KeyResult> findByArchivedFalse(Pageable pageable);
}
