package com.application.jpa.web.rest;

import com.application.jpa.domain.Attachment;
import com.application.jpa.domain.enumeration.FileType;
import com.application.jpa.service.AttachmentService;
import com.application.jpa.web.rest.exception.BusinessErrorException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.application.jpa.domain.enumeration.BusinessErrorType.PARAMETER_EXCEPTION;

@Api(value = "Attachment", tags = {"Attachment文件管理接口"})
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AttachmentResources {
    private final AttachmentService attachmentService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileType", paramType = "form", value = "文件类型"),
            @ApiImplicitParam(name = "files", value = "文件流对象,接收数组格式", required = true,
                    dataTypeClass = MultipartFile[].class, allowMultiple = true)
    })
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @PostMapping("/file/upload")
    @Timed
    public Attachment[] uploadFile(@RequestParam(value = "fileType", required = false) FileType fileType,
                                   MultipartFile[] files) throws IOException {
        if (ObjectUtils.isEmpty(files)) {
            throw new BusinessErrorException(PARAMETER_EXCEPTION);
        }
        Attachment[] attachments = null;
        attachments = attachmentService.saveToDisk(files);
        //attachmentService.saveToDataBase(files);
        //attachments = attachmentService.saveToMongoDB(files);
        //attachmentService.saveToRedis(files);
        return attachments;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", required = true,
                    paramType = "path", example = "123456", dataTypeClass = String.class)}
    )
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "下载文件(mongodb)", notes = "下载文件(mongodb)")
    @GetMapping(value = "/file/{id}/mongodb", produces = {"application/octet-stream", MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @Timed
    public void findToMongoDB(@PathVariable("id") String id,
                              HttpServletResponse response) throws IOException {
        attachmentService.findToMongoDB(id, response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "文件url地址", required = true,
                    paramType = "path", example = "image/jpeg/123453525.jpg", dataTypeClass = String.class)}
    )
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "下载文件(disk)", notes = "下载文件(disk)")
    @GetMapping(value = "/file/{url}", produces = {"application/octet-stream", MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @Timed
    public void findToDisk(@PathVariable("url") String url,
                           HttpServletResponse response) {
        attachmentService.findToDisk(url, response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)}
    )
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "下载文件(database)", notes = "下载文件(database)")
    @GetMapping(value = "/file/{id}/database", produces = {"application/octet-stream", MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @Timed
    public void findToDataBase(@PathVariable("id") Long id,
                               HttpServletResponse response) {
        attachmentService.findToDataBase(id, response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)}
    )
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "下载文件(redis)", notes = "下载文件(redis)")
    @GetMapping(value = "/file/{id}/redis", produces = {"application/octet-stream", MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @Timed
    public void findToRedis(@PathVariable("id") String id,
                            HttpServletResponse response) {
        attachmentService.findToRedis(id, response);
    }
}
