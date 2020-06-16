package com.application.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@ApiModel(value = "Authority", description = "权限")
@Entity
@Table(name = "tbl_authority")
@Setter
@Getter
@EqualsAndHashCode(exclude = {"roles"})
@ToString(exclude = {"roles"})
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class Authority implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "权限id", required = true, dataType = "Long", example = "1")
    private Long id;

    @NotNull(message = "名称不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "权限名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "name", value = "权限名", required = true, dataType = "String", example = "系统锁定权限(权限名不能为空或超过20个字符)")
    private String name;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @BatchSize(size = 20)
    @JsonIgnoreProperties({"authorities"})
    @OrderBy("id asc")
    private Set<Role> roles = new LinkedHashSet<>(10);

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "权限版本锁", example = "0", dataType = "Long", required = true)
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public Authority addRole(Role role) {
        this.roles.add(role);
        role.getAuthorities().add(this);
        return this;
    }

    public Authority addAllRole(Set<Role> roles) {
        this.roles.addAll(roles);
        this.roles.forEach(n -> n.getAuthorities().add(this));
        return this;
    }

    public Authority removeRole(Role role) {
        role.getAuthorities().remove(role);
        this.roles.remove(role);
        return this;
    }

    public Authority removeRoles() {
        Iterator<Role> iterator = this.roles.iterator();
        while (iterator.hasNext()) {
            Role next = iterator.next();
            next.getAuthorities().remove(this);
            iterator.remove();
        }
        return this;
    }
}
