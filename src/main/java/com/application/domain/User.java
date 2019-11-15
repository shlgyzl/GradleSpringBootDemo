package com.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

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
@ApiModel(value = "User", description = "User基础信息")
@EqualsAndHashCode(exclude = {"dams"})
@ToString(exclude = {"dams"})
public class User implements Serializable {


    private static final long serialVersionUID = 1821697803716075855L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    @ApiModelProperty(value = "账号")
    private String login;

    //@NotNull
    //@Size(min = 60, max = 60)
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
    private Set<Dam> dams = new HashSet<>();
}
