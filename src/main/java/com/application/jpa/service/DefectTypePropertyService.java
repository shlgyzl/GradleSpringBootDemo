package com.application.jpa.service;

import com.application.jpa.domain.DefectTypeProperty;
import com.application.jpa.domain.QDefectTypeProperty;
import com.application.jpa.repository.DefectTypePropertyRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 使用JetCache
 */
@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class DefectTypePropertyService {
    private final DefectTypePropertyRepository defectTypePropertyRepository;

    public DefectTypeProperty save(DefectTypeProperty defectTypeProperty) {
        return defectTypePropertyRepository.save(defectTypeProperty);
    }

    public DefectTypeProperty update(DefectTypeProperty defectTypeProperty) {
        return defectTypePropertyRepository.save(defectTypeProperty);
    }

    public void delete(Long id) {
        defectTypePropertyRepository.deleteById(id);
    }

    public DefectTypeProperty find(Long id) {
        return defectTypePropertyRepository.findById(id).orElse(null);
    }

    // 开启自动刷新缓存机制
    public Page<DefectTypeProperty> findAll(DefectTypeProperty defectTypeProperty, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(defectTypeProperty)) {
            QDefectTypeProperty qDefectTypeProperty = QDefectTypeProperty.defectTypeProperty;
            if (StringUtils.hasText(defectTypeProperty.getName())) {
                builder.and(qDefectTypeProperty.name.containsIgnoreCase(defectTypeProperty.getName()));
            }
        }
        return defectTypePropertyRepository.findAll(builder, pageable);
    }
}
