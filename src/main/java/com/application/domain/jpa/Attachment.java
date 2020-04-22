package com.application.domain.jpa;

import com.application.domain.abstracts.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ApiModelProperty(name = "id", value = "文件id", required = true, dataType = "Long", example = "1")
    private Long id;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "fileName", value = "文件名称", required = true, dataType = "String", example = "测试文档")
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "simpleName", value = "文件简称", required = true, dataType = "String", example = "测试")
    @Column(name = "simple_name", nullable = false)
    private String simpleName;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "originalFileName", value = "文件原名称", required = true, dataType = "String", example = "测试文档")
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "fileSize", value = "文件大小(字节数)", required = true, dataType = "Long", example = "1024")
    @Column(name = "file_size", nullable = false)
    private Long fileSize;


    @NotNull
    @NonNull
    @ApiModelProperty(name = "fileSizeFormatted", value = "文件大小(带有单位)", required = true, dataType = "String", example = "1M")
    @Column(name = "file_size_formatted", nullable = false)
    private String fileSizeFormatted;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "contentType", value = "文件类型", required = true, dataType = "String", example = "image/jpeg")
    @Column(name = "content_type", nullable = false)
    private String contentType;

    @ApiModelProperty(name = "content", value = "文件内容(blob/longblob)", required = true, dataType = "byte[]")
    @Column(name = "content", nullable = false)
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @ApiModelProperty(name = "file", value = "文件内容(longblob)", required = true, dataType = "File")
    @Column(name = "file")
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private File file;

    @ApiModelProperty(name = "stringContent", value = "文件内容(text/longtext)", required = true, dataType = "String")
    @Column(name = "string_content")
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String stringContent;

    @ApiModelProperty(name = "image", value = "文件图片(longblob)", required = true, dataType = "Image")
    @Column(name = "image")
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Image image;

    @ApiModelProperty(name = "filePath", value = "文件路径", required = true, dataType = "String", example = "C:\\WorkSpace\\测试文档.txt")
    @Column(name = "file_path")
    private String filePath;


    @ApiModelProperty(name = "url", value = "文件网络路径", required = true, dataType = "String", example = "/api/file/{1}/disk")
    @Column(name = "url")
    private String url;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "文件版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;
}
