package com.application.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DefectType.
 */
@Data
@Table(name = "tbl_defect_type")
@Entity
@ApiModel(value = "DefectType", description = "缺陷类型")
@EqualsAndHashCode(exclude = {"defectTypeProperties"})
@ToString(exclude = {"defectTypeProperties"})
@NoArgsConstructor
@RequiredArgsConstructor
public class DefectType implements Serializable {

    private static final long serialVersionUID = 1584568401007494606L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @NonNull
    @ApiModelProperty(name = "name", value = "缺陷类型名称", dataType = "String", required = true)
    @Column(nullable = false)
    private String name;


    @NotNull
    @NonNull
    @ApiModelProperty(name = "code", value = "缺陷类型编号", dataType = "String", required = true)
    @Column(nullable = false)
    private String code;


    @ManyToOne
    @ApiModelProperty(name = "name", value = "大坝", dataType = "String")
    private Dam dam;

    /**
     * 这里对级联类型说明一下：
     * MERGE:表示更新，也就是说如果是更新操作,大家都一起更新,如果是新增大家一起新增
     * REMOVE:表示删除该实体的同时也会删除该集合及其关系和关系的实体
     * REFRESH:表示在操作数据之前会先查询一次该集合的最新数据,防止多人操作实体及其关系但是没有及时更新到本次操作中
     * DETACH:表示该实体会撤销集合元素上的外键管理,(此操作需要删除才能生效,不明确)
     */
    @OneToMany(mappedBy = "defectType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH})
    @ApiModelProperty(name = "defectTypeProperties", value = "缺陷类型属性集合", dataType = "String")
    private Set<DefectTypeProperty> defectTypeProperties = new HashSet<>();

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "缺陷类型版本锁", dataType = "Long", required = true)
    @Column
    @Version
    private Long version;
}
