package com.application.domain.jpa;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会议文件
 */
@ApiModel(description = "会议文件")
@Entity
@Table(name = "attachment")
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件名称
     */
    @NotNull
    @ApiModelProperty(value = "文件名称", required = true)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * 文件大小(字节数)
     */
    @NotNull
    @ApiModelProperty(value = "文件大小(字节数)", required = true)
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    /**
     * 文件大小(带有单位的大小)
     */
    @NotNull
    @ApiModelProperty(value = "(带有单位的大小)", required = true)
    @Column(name = "file_size_formatted", nullable = false)
    private String fileSizeFormatted;

    /**
     * 文件类型
     */
    @NotNull
    @ApiModelProperty(value = "文件类型", required = true)
    @Column(name = "content_type", nullable = false)
    private String contentType;

    /**
     * 文件路径
     */
    @NotNull
    @ApiModelProperty(value = "文件路径", required = true)
    @Column(name = "file_path", nullable = false)
    private String filePath;
}
