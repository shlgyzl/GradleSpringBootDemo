package com.application.web.service;

import com.application.web.domain.jpa.DefectType;
import com.application.web.repository.jpa.DefectTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;

    public DefectTypeService(DefectTypeRepository defectTypeRepository) {
        this.defectTypeRepository = defectTypeRepository;
    }

    @CachePut(value = "redisCache",key = "#defectType")
    public DefectType saveOrUpdate(DefectType defectType) {
        defectType.addAllDefectTypeProperty(defectType.getDefectTypeProperties());
        return defectTypeRepository.save(defectType);
    }
}
