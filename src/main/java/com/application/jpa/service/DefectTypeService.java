package com.application.jpa.service;

import com.application.jpa.domain.DefectType;
import com.application.jpa.domain.QDefectType;
import com.application.jpa.repository.DefectTypeRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;


    public DefectType saveOrUpdate(DefectType defectType) {
        defectType.addAllDefectTypeProperty(defectType.getDefectTypeProperties());
        return defectTypeRepository.save(defectType);
    }

    public Page<DefectType> findAll(DefectType defectType, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(defectType)) {
            QDefectType qDefectType = QDefectType.defectType;
            if (StringUtils.hasText(defectType.getName())) {
                builder.and(qDefectType.name.containsIgnoreCase(defectType.getName()));
            }
        }
        return defectTypeRepository.findAll(builder, pageable);
    }
}
