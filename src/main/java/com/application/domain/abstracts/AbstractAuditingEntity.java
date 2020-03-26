package com.application.domain.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    //@Temporal(TemporalType.TIMESTAMP)//生成yyyy-MM-dd类型的日期实体类上必须有Date类型或calendar类型才有效
    //@JsonFormat(pattern = "yyyy-MM-dd")//出参时间格式化
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")//入参时，请求报文只需要传入yyyy mm dd hh mm ss字符串进来，则自动转换为Date类型数据
    @Column(name = "created_date", updatable = false)
    //@JsonIgnore
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.systemDefault());

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    //@Temporal(TemporalType.TIMESTAMP)//生成yyyy-MM-dd类型的日期
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//出参时间格式化
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")//入参时，请求报文只需要传入yyyy mm dd hh mm ss字符串进来，则自动转换为Date类型数据
    @Column(name = "last_modified_date")
    //@JsonIgnore
    private LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneId.systemDefault());
}
