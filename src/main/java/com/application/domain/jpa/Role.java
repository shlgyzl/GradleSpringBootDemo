package com.application.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_role")
@Data
@ApiModel(value = "Role", description = "角色")
@ToString(exclude = {"authorities"})
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Role implements Serializable {

    private static final long serialVersionUID = -3049726724207267178L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "名称不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "角色名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "name", value = "角色名", dataType = "String")
    private String name;

    @JoinTable(
            name = "tbl_role_authority",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Authority> authorities = new LinkedHashSet<>();

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "角色版本锁", dataType = "Long", required = true)
    @Column
    @Version
    @JsonIgnore
    private Long version = 0L;
}
