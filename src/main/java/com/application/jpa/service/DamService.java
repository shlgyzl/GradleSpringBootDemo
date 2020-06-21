package com.application.jpa.service;

import com.application.jpa.domain.Dam;
import com.application.jpa.domain.QDam;
import com.application.jpa.repository.DamRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class DamService {
    private final DamRepository damRepository;

    public Dam save(Dam dam) {
        return damRepository.save(dam);
    }

    public Dam update(Dam dam) {
        return damRepository.save(dam);
    }

    public void delete(Long id) {
        damRepository.deleteById(id);
    }

    public Dam find(Long id) {
        return damRepository.findById(id).orElse(null);
    }

    public Page<Dam> findAll(Dam dam, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(dam)) {
            QDam qDam = QDam.dam;
            if (StringUtils.hasText(dam.getName())) {
                builder.and(qDam.name.containsIgnoreCase(dam.getName()));
            }
        }
        return damRepository.findAll(builder, pageable);
    }
}
