package com.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
//@Document(collection = "user")
@Table(name = "tbl_user")
@Data
@ApiModel(value = "User", description = "用户")
@EqualsAndHashCode(exclude = {"dams"},callSuper = true)
@ToString(exclude = {"dams"})
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends AbstractAuditingEntity implements Serializable {


    private static final long serialVersionUID = 1821697803716075855L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NonNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    @ApiModelProperty(value = "账号")
    private String login;

    @NotNull
    @NonNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "tbl_user_dam",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dam_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @DBRef
    private Set<Dam> dams = new HashSet<>();

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "用户版本锁", dataType = "Long", required = true)
    @Column
    @Version
    private Long version;
}
