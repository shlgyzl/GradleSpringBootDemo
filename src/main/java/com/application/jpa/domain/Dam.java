package com.application.jpa.domain;

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

@ApiModel(value = "Dam", description = "大坝")
@Entity
@Table(name = "tbl_dam")
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Dam implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "大坝id", required = true, dataType = "Long", example = "1", notes = "大坝id必须存在")
    private Long id;

    @NotNull(message = "大坝名称不能为空")
    @NonNull
    @ApiModelProperty(name = "name", value = "大坝名称", required = true, dataType = "String", example = "三峡大坝")
    @Column(nullable = false)
    private String name;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "大坝版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;
}
