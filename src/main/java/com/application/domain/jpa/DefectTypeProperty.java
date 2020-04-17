package com.application.domain.jpa;

import com.application.domain.enumeration.FieldType;
import com.application.domain.enumeration.Region;
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

/**
 * A DefectTypeProperty.
 */

@Data
@Table(name = "tbl_defect_type_property")
@Entity
@ApiModel(value = "DefectType", description = "缺陷类型属性")
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
    @ApiModelProperty(name = "id", value = "缺陷类型属性id", dataType = "Long", required = true, notes = "缺陷类型属性id必须存在")
    private Long id;


    @NotNull(message = "缺陷类型属性名称不能为空")
    @NonNull
    @ApiModelProperty(name = "name", value = "缺陷类型属性名称", dataType = "String", required = true)
    @Column(nullable = false)
    private String name;


    @NotNull(message = "字段类型不能为空")
    @NonNull
    @ApiModelProperty(name = "fieldType", value = "字段类型", dataType = "String", required = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    @NotNull(message = "必填项不能为空")
    @NonNull
    @ApiModelProperty(name = "requiredField", value = "必填项", dataType = "Boolean")
    @Column(name = "required_field")
    private Boolean requiredField = false;


    @ApiModelProperty(name = "maxValue", value = "最大值", dataType = "String")
    @Column(name = "max_value")
    private String maxValue;


    @ApiModelProperty(name = "minValue", value = "最小值", dataType = "String")
    @Column(name = "min_value")
    private String minValue;


    @ApiModelProperty(name = "defaultValue", value = "默认值", dataType = "String")
    @Column(name = "default_value")
    private String defaultValue;


    @ApiModelProperty(name = "value", value = "测量值", dataType = "String")
    @Column(name = "value")
    private String value;


    @ApiModelProperty(name = "unit", value = "单位", dataType = "String")
    @Column(name = "unit")
    private String unit;


    @ApiModelProperty(name = "defectType", value = "缺陷类型", dataType = "DefectType", hidden = true)
    @JsonIgnoreProperties(value = {"defectTypeProperties"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "defect_type_id")
    private DefectType defectType;

    @ApiModelProperty(name = "region", value = "值所在区间", dataType = "String")
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private Region region;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "缺陷类型属性版本锁", example = "0L", dataType = "Long", required = true, hidden = true)
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public void addDefectType(DefectType defectType) {
        this.defectType = defectType;
        defectType.getDefectTypeProperties().add(this);
    }

    public void removeDefectType(DefectType defectType) {
        this.defectType = null;
        defectType.getDefectTypeProperties().remove(this);
    }
}
