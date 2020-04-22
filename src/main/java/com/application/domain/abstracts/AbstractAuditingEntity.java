package com.application.domain.abstracts;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "createdBy", value = "创建人", required = true, dataType = "String", example = "超级管理员")
    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;

    //@Temporal(TemporalType.TIMESTAMP)//生成yyyy-MM-dd类型的日期实体类上必须有Date类型或calendar类型才有效
    //@JsonFormat(pattern = "yyyy-MM-dd")//出参时间格式化
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")//入参时，请求报文只需要传入yyyy mm dd hh mm ss字符串进来，则自动转换为Date类型数据
    @ApiModelProperty(name = "createdDate", value = "创建时间", required = true, dataType = "LocalDateTime", example = "2020-06-06 22:12:49")
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.systemDefault());

    @ApiModelProperty(name = "lastModifiedBy", value = "最后一次修改人", required = true, dataType = "String", example = "超级管理员")
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    //@Temporal(TemporalType.TIMESTAMP)//生成yyyy-MM-dd类型的日期
    //@JsonFormat(pattern = "yyyy-MM-dd")//出参时间格式化(这种只是局部覆盖全局)
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")//入参时，请求报文只需要传入yyyy mm dd hh mm ss字符串进来，则自动转换为Date类型数据
    @ApiModelProperty(name = "lastModifiedDate", value = "最后一次修改时间", required = true, dataType = "LocalDateTime", example = "2022-06-06 22:12:49")
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneId.systemDefault());
}
