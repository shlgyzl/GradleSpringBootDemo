package com.application.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_role")
@Data
@EqualsAndHashCode(exclude = {"authorities", "users"})
@ApiModel(value = "Role", description = "角色")
@ToString(exclude = {"authorities", "users"})
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "角色名称不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "角色名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "name", value = "角色名", dataType = "String")
    private String name;

    @JoinTable(
            name = "tbl_role_authority",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"roles"})
    @OrderBy("id asc")
    private Set<Authority> authorities = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"roles"})
    private Set<User> users = new LinkedHashSet<>();

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "角色版本锁", example = "0L", dataType = "Long", required = true)
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public void addAllAuthority(Set<Authority> authorities) {
        this.authorities.addAll(authorities);
        authorities.forEach(n -> n.getRoles().add(this));
    }
}
