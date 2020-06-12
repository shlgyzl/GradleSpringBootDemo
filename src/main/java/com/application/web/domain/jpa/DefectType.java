package com.application.web.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A DefectType.
 */
//@ApiModel(value = "DefectType", description = "缺陷类型")

@Table(name = "tbl_defect_type")
@Entity
@Setter
@Getter
@EqualsAndHashCode(exclude = {"defectTypeProperties"}, callSuper = false)
@ToString(exclude = {"defectTypeProperties"})
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class DefectType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "缺陷类型id", required = true, dataType = "Long", example = "1")
    private Long id;


    @NotNull(message = "缺陷类型名称不能为空")
    @NonNull
    @ApiModelProperty(name = "name", value = "缺陷类型名称", required = true, dataType = "String", example = "下坡漏水")
    @Column(nullable = false)
    private String name;


    @NotNull(message = "缺陷类型编号不能为空")
    @NonNull
    @ApiModelProperty(name = "code", value = "缺陷类型编号", required = true, dataType = "String", example = "001")
    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(name = "dam", value = "大坝", required = true, dataType = "Dam")
    @JoinColumn(name = "dam_id")
    private Dam dam;

    @OneToMany(mappedBy = "defectType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy(value = "id asc")
    @JsonIgnoreProperties({"defectType"})
    private Set<DefectTypeProperty> defectTypeProperties = new HashSet<>();

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "缺陷类型版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public DefectType addDefectTypeProperty(DefectTypeProperty defectTypeProperty) {
        this.defectTypeProperties.add(defectTypeProperty);
        defectTypeProperty.setDefectType(this);
        return this;
    }

    public DefectType addAllDefectTypeProperty(Set<DefectTypeProperty> defectTypeProperties) {
        this.defectTypeProperties.addAll(defectTypeProperties);
        defectTypeProperties.forEach(n -> n.setDefectType(this));
        return this;
    }

    public DefectType removeDefectTypeProperty(DefectTypeProperty defectTypeProperty) {
        defectTypeProperty.setDefectType(null);
        this.defectTypeProperties.remove(defectTypeProperty);
        return this;
    }

    public DefectType removeDefectTypeProperties() {
        Iterator<DefectTypeProperty> iterator = this.defectTypeProperties.iterator();
        while (iterator.hasNext()) {
            DefectTypeProperty next = iterator.next();
            next.setDefectType(null);
            iterator.remove();
        }
        return this;
    }
}
