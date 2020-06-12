package com.application.web.domain.jpa;

import com.application.web.domain.abstracts.AbstractAuditingEntity;
import com.application.listener.UserAuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A user.
 */
//@ApiModel(value = "User", description = "用户")
@Entity
@Table(name = "tbl_user")
@Setter
@Getter
@EqualsAndHashCode(exclude = {"dams"}, callSuper = true)
@ToString(exclude = {"dams"})
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@EntityListeners(UserAuditListener.class)
public class User extends AbstractAuditingEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "用户id", required = true, dataType = "Long", example = "1")
    private Long id;

    @NotNull(message = "账号不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "用户名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "login", value = "账号", required = true, dataType = "String", example = "admin")
    private String login;

    //@NotNull(message = "密码不能为空")
    @NonNull
    //@Size(min = 6, max = 15, message = "密码不能小于6个字符,不能超过15个字符")//不能与JsonIgnore同时使用
    @Column(name = "password_hash", length = 15, nullable = false)
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String", example = "admin")
    @JsonIgnore
    private String password = ((ThreadLocalRandom.current().nextInt() * 9 + 1) * 100000) + "";


    @NotNull
    @NonNull
    @ApiModelProperty(name = "activated", value = "是否启用", required = true, dataType = "Boolean", example = "false")
    @Column(name = "activated", nullable = false)
    private Boolean activated = false;

    @ApiModelProperty(name = "imageUrl", value = "头像", required = true, dataType = "String", example = "/api/file/image/jpeg/头像.jpg")
    @Column(name = "image_url")
    private String imageUrl;

    @JoinTable(
            name = "tbl_user_dam",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dam_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("id asc")
    @ApiModelProperty(hidden = true)
    private Set<Dam> dams = new LinkedHashSet<>(5);

    @JoinTable(
            name = "tbl_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"users"})
    @OrderBy("id asc")
    private Set<Role> roles = new LinkedHashSet<>(5);

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "用户版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;

    public User addAllRole(Set<Role> roles) {
        this.roles.addAll(roles);
        roles.forEach(n -> n.getUsers().add(this));
        return this;
    }
}
