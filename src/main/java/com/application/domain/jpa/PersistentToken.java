package com.application.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

//@ApiModel(value = "PersistentToken", description = "持久化Token")
@Entity
@Table(name = "tbl_persistent_token")
@Setter
@Getter
@ToString(exclude = {"user"})
@EqualsAndHashCode(exclude = {"user"})
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class PersistentToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String Id;

    @JsonIgnore
    @NotNull(message = "token值不能为空")
    @NonNull
    @ApiModelProperty(name = "tokenValue", value = "token值", required = true, dataType = "String")
    @Column(name = "token_value", nullable = false)
    private String tokenValue;

    @NotNull(message = "token有效期不能为空")
    @NonNull
    @ApiModelProperty(name = "tokenDate", value = "token有效期", required = true, dataType = "LocalDate")
    @Column(name = "token_date")
    private LocalDate tokenDate;

    @NotNull(message = "ip地址不能为空")
    @NonNull
    @Size(max = 39)
    @ApiModelProperty(name = "ipAddress", value = "ip地址", required = true, dataType = "String")
    @Column(name = "ip_address", length = 39)
    private String ipAddress;

    @NotNull(message = "用户代理不能为空")
    @NonNull
    @ApiModelProperty(name = "userAgent", value = "用户代理", required = true, dataType = "String")
    @Column(name = "user_agent")
    private String userAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(name = "user", value = "用户", required = true, dataType = "User")
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "持久化Token版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;
}
