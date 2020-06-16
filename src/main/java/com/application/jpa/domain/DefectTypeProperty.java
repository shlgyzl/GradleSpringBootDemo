package com.application.jpa.domain;

import com.application.jpa.domain.enumeration.FieldType;
import com.application.jpa.domain.enumeration.Region;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

import static com.application.jpa.domain.enumeration.FieldType.TEXT;
import static com.application.jpa.domain.enumeration.Region.LT;

/**
 * A DefectTypeProperty.
 */

@ApiModel(value = "DefectType", description = "缺陷类型属性")
@Table(name = "tbl_defect_type_property")
@Entity
@Setter
@Getter
@EqualsAndHashCode(exclude = {"defectType"}, callSuper = false)
@ToString(exclude = {"defectType"})
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class DefectTypeProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "缺陷类型属性id", required = true, dataType = "Long", example = "1")
    private Long id;


    @NotNull(message = "缺陷类型属性名称不能为空")
    @NonNull
    @ApiModelProperty(name = "name", value = "缺陷类型属性名称", required = true, dataType = "String", example = "渗水量")
    @Column(nullable = false)
    private String name;

    @ApiModelProperty(name = "fieldType", value = "字段类型", required = true, dataType = "String", example = "TEXT", allowableValues = "TEXT,NUMBER")
    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType = TEXT;

    @ApiModelProperty(name = "requiredField", value = "必填项", required = true, dataType = "Boolean", example = "false")
    @Column(name = "required_field", nullable = false)
    private Boolean requiredField = false;

    @ApiModelProperty(name = "maxValue", value = "最大值", required = true, dataType = "String", example = "0")
    @Column(name = "max_value", nullable = false)
    private BigDecimal maxValue = BigDecimal.ZERO;

    @ApiModelProperty(name = "minValue", value = "最小值", required = true, dataType = "String", example = "10")
    @Column(name = "min_value", nullable = false)
    private BigDecimal minValue = BigDecimal.ONE;

    @ApiModelProperty(name = "defaultValue", value = "默认值", required = true, dataType = "String", example = "10立方米")
    @Column(name = "default_value")
    private String defaultValue;

    @ApiModelProperty(name = "value", value = "测量值", required = true, dataType = "String", example = "10立方米")
    @Column(name = "value")
    private String value;

    @ApiModelProperty(name = "unit", value = "单位", required = true, dataType = "String", example = "立方米", notes = "改值配合Number类型")
    @Column(name = "unit")
    private String unit;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "defectType", value = "缺陷类型", required = true, dataType = "DefectType")
    @JsonIgnoreProperties(value = {"defectTypeProperties"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "defect_type_id")
    private DefectType defectType;

    @ApiModelProperty(name = "region", value = "值所在区间", required = true, dataType = "String", example = "LT", allowableValues = "LT, LG, GT", notes = "此值配合数值类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private Region region = LT;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "缺陷类型属性版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public DefectTypeProperty addDefectType(DefectType defectType) {
        this.defectType = defectType;
        defectType.getDefectTypeProperties().add(this);
        return this;
    }

    public DefectTypeProperty removeDefectType(DefectType defectType) {
        this.defectType = null;
        defectType.getDefectTypeProperties().remove(this);
        return this;
    }
}
