package com.application.hibernate.lock.domain;

/**
 * 测试锁
 *
 * @author yanghaiyong
 * 2020/7/12-7:54
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A user.
 */
@ApiModel(value = "HibernateUser", description = "Hibernate用户")
@Entity
@Table(name = "tbl_hibernate_user")
@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.ALL)// 表示更新会进行所有字段校验版本,且需要注解@DynamicUpdate和不需要注解@Version
/**
 * @OptimisticLocking(type = OptimisticLockType.DIRTY)// 表示更新会进行部分字段校验版本,且需要注解@DynamicUpdate,@SelectBeforeUpdate和不需要注解@Version
 * @SelectBeforeUpdate// 以便Session#update(entity)操作可以正确处理分离的实体
 */
public class HibernateUser {
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


    @ApiModelProperty(name = "imageUrl", value = "头像", required = true, dataType = "String", example = "/api/file/image/jpeg/头像.jpg")
    @Column(name = "image_url")
    @OptimisticLock(excluded = true)// 不会因为修改而出发版本锁增加
    private String imageUrl;


    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "用户版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    //@Version
    //@Source(SourceType.DB)// 默认是数据库生成,只能由日期属性Date使用
    //@org.hibernate.annotations.Generated(GenerationTime.ALWAYS)// 如果是时间戳,那么使用此注解将会默认使用数据库生成
    private Long version = 0L;
}
