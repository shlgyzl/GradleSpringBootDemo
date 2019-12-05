package com.application.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
//@Document(collection = "dam")
@Table(name = "tbl_dam")
@Data
@ApiModel(value = "Dam", description = "大坝")
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Dam implements Serializable {
    private static final long serialVersionUID = -4772373087949409931L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "大坝id", dataType = "Long", required = true, notes = "大坝id必须存在")
    private Long id;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "name", value = "大坝名称", dataType = "String", required = true)
    @Column(nullable = false)
    private String name;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "大坝版本锁", dataType = "Long", required = true)
    @Column
    @Version
    private Long version;
}
