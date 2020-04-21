package com.application.service;

import com.application.domain.jpa.DefectType;
import com.application.repository.jpa.DefectTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;

    public DefectTypeService(DefectTypeRepository defectTypeRepository) {
        this.defectTypeRepository = defectTypeRepository;
    }

    public DefectType saveOrUpdate(DefectType defectType) {
        defectType.addAllDefectTypeProperty(defectType.getDefectTypeProperties());
        return defectTypeRepository.save(defectType);
    }
}
