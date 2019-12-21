package com.application.domain;

import com.application.domain.enumeration.FieldType;
import com.application.domain.enumeration.Region;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DefectTypeProperty.
 */

@Data
@Table(name = "tbl_defect_type_property")
@Entity
@ApiModel(value = "DefectType", description = "缺陷类型属性")
@EqualsAndHashCode(exclude = {"defectType"})
@ToString(exclude = {"defectType"})
@NoArgsConstructor
@RequiredArgsConstructor
public class DefectTypeProperty implements Serializable {

    private static final long serialVersionUID = 5939254100740949152L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "缺陷类型属性id", dataType = "Long", required = true, notes = "缺陷类型属性id必须存在")
    private Long id;


    @NotNull
    @NonNull
    @ApiModelProperty(name = "name", value = "缺陷类型属性名称", dataType = "String", required = true)
    @Column(nullable = false)
    private String name;


    @NotNull
    @NonNull
    @ApiModelProperty(name = "fieldType", value = "字段类型", dataType = "String", required = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "requiredField", value = "必填项", dataType = "Boolean")
    @Column(name = "required_field")
    private Boolean requiredField;


    @ApiModelProperty(name = "maxValue", value = "最大值", dataType = "String")
    @Column
    private String maxValue;


    @ApiModelProperty(name = "minValue", value = "最小值", dataType = "String")
    @Column
    private String minValue;


    @ApiModelProperty(name = "defaultValue", value = "默认值", dataType = "String")
    @Column
    private String defaultValue;


    @ApiModelProperty(name = "value", value = "测量值", dataType = "String")
    @Column
    private String value;


    @ApiModelProperty(name = "unit", value = "单位", dataType = "String")
    @Column
    private String unit;


    @ApiModelProperty(name = "defectType", value = "缺陷类型", dataType = "DefectType", hidden = true)
    @ManyToOne
    @JsonIgnoreProperties(value = {"defectTypeProperties"})
    private DefectType defectType;


    @ApiModelProperty(name = "region", value = "值所在区间", dataType = "String")
    @Enumerated(EnumType.STRING)
    @Column
    private Region Region;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "缺陷类型属性版本锁", dataType = "Long", required = true, hidden = true)
    @Column
    @Version
    private Long version = 0L;
}
