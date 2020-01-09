package com.application.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tbl_dam")
@Data
@ApiModel(value = "Dam", description = "大坝")
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@DynamicInsert
@DynamicUpdate
public class Dam implements Serializable {
    private static final long serialVersionUID = -4772373087949409931L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "大坝id", dataType = "Long", required = true, notes = "大坝id必须存在")
    private Long id;

    @NotNull(message = "大坝名称不能为空")
    @NonNull
    @ApiModelProperty(name = "name", value = "大坝名称", dataType = "String", required = true)
    @Column(nullable = false)
    private String name;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "大坝版本锁", dataType = "Long", required = true, hidden = true)
    @Column
    @Version
    @JsonIgnore
    private Long version = 0L;
}
