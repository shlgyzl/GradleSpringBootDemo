package com.application.domain.jpa;

import com.application.domain.abstracts.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "tbl_user")
@Data
@ApiModel(value = "User", description = "用户")
@EqualsAndHashCode(exclude = {"dams"}, callSuper = true)
@ToString(exclude = {"dams"})
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User extends AbstractAuditingEntity implements Serializable {


    private static final long serialVersionUID = 1821697803716075855L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "账号不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "用户名不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "login", value = "账号", dataType = "String")
    private String login;

    //@NotNull(message = "密码不能为空")
    @NonNull
    //@Size(min = 6, max = 15, message = "密码不能小于6个字符,不能超过15个字符")//不能与JsonIgnore同时使用
    @Column(name = "password_hash", length = 15, nullable = false)
    @ApiModelProperty(name = "password", value = "密码", dataType = "String")
    @JsonIgnore
    private String password = (int) ((Math.random() * 9 + 1) * 100000) + "";


    @JoinTable(
            name = "tbl_user_dam",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dam_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private Set<Dam> dams = new HashSet<>();

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "用户版本锁", dataType = "Long", required = true)
    @Column
    @Version
    @JsonIgnore
    private Long version = 0L;
}
