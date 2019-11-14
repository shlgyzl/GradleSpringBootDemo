package com.application.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_dam")
@Data
@ApiModel(value = "Dam", description = "Dam基础信息")
public class Dam implements Serializable {
    private static final long serialVersionUID = -4772373087949409931L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "大坝id", dataType = "Long", required = true, notes = "大坝id必须存在")
    private Long id;

    @Column
    @ApiModelProperty(name = "name")
    private String name;

}
