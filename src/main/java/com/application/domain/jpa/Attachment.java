package com.application.domain.jpa;

import com.application.domain.abstracts.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.File;
import java.io.Serializable;

/**
 * 会议文件
 */
//@ApiModel(value = "Attachment",description = "会议文件")
@Entity
@Table(name = "tbl_attachment")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
public class Attachment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件名称", required = true)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件简称", required = true)
    @Column(name = "simple_name", nullable = false)
    private String simpleName;

    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件原名称", required = true)
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件大小(字节数)", required = true)
    @Column(name = "file_size", nullable = false)
    private Long fileSize;


    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件大小(带有单位)", required = true)
    @Column(name = "file_size_formatted", nullable = false)
    private String fileSizeFormatted;


    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件类型", required = true)
    @Column(name = "content_type", nullable = false)
    private String contentType;

    @NotNull
    @NonNull
    @ApiModelProperty(value = "文件内容(blob/longblob)", required = true)
    @Column(name = "content", nullable = false)
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @ApiModelProperty(value = "文件内容(longblob)", required = true)
    @Column(name = "file")
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private File file;

    @ApiModelProperty(value = "文件内容(text/longtext)", required = true)
    @Column(name = "string_content")
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String stringContent;

    @ApiModelProperty(value = "文件图片(longblob)", required = true)
    @Column(name = "image")
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Image image;

    @ApiModelProperty(value = "文件路径", required = true)
    @Column(name = "file_path")
    private String filePath;


    @ApiModelProperty(value = "文件网络路径", required = true)
    @Column(name = "url")
    private String url;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "文件版本锁", example = "0L", dataType = "Long", required = true, hidden = true)
    @Column(name = "version")
    @Version
    private Long version = 0L;
}
