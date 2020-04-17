package com.application.domain.jpa;

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

@Entity
@Table(name = "tbl_authority")
@Data
@EqualsAndHashCode(exclude = {"roles"})
@ApiModel(value = "Authority", description = "权限")
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
    private Long id;

    @NotNull(message = "名称不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "权限名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "name", value = "权限名", dataType = "String")
    private String name;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @BatchSize(size = 20)
    @JsonIgnoreProperties({"authorities"})
    @OrderBy("id asc")
    private Set<Role> roles = new LinkedHashSet<>(10);

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "权限版本锁", example = "0L", dataType = "Long", required = true)
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public void addRole(Role role) {
        this.roles.add(role);
        role.getAuthorities().add(this);
    }

    public void addAllRole(Set<Role> roles) {
        this.roles.addAll(roles);
        this.roles.forEach(n -> n.getAuthorities().add(this));
    }

    public void removeRole(Role role) {
        role.getAuthorities().remove(role);
        this.roles.remove(role);
    }

    public void removeRoles() {
        Iterator<Role> iterator = this.roles.iterator();
        while (iterator.hasNext()) {
            Role next = iterator.next();
            next.getAuthorities().remove(this);
            iterator.remove();
        }
    }
}
