package com.application.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_authority")
@Data
@EqualsAndHashCode(exclude = {"roles"})
@ApiModel(value = "Authority", description = "权限")
@ToString(exclude = {"roles"})
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Authority implements Serializable {
    private static final long serialVersionUID = 417152214494393977L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "名称不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "权限名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "name", value = "权限名", dataType = "String")
    private String name;

    @ManyToMany(mappedBy = "authorities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @BatchSize(size = 20)
    @JsonIgnoreProperties({"authorities"})
    @OrderBy("id asc")
    private Set<Role> roles = new LinkedHashSet<>(10);

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "权限版本锁", dataType = "Long", required = true)
    @Column
    @Version
    private Long version = 0L;
}
