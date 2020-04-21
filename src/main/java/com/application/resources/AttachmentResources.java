package com.application.resources;

import com.application.domain.enumeration.FileType;
import com.application.domain.jpa.Attachment;
import com.application.resources.exception.BusinessErrorException;
import com.application.service.AttachmentService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.application.domain.enumeration.BusinessErrorType.PARAMETER_EXCEPTION;

@Api(value = "Attachment", tags = {"Attachment文件管理接口"})
@RestController
@RequestMapping("/api")
public class AttachmentResources {
    private final AttachmentService attachmentService;

    public AttachmentResources(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @PostMapping("/file/upload")
    @Timed
    public Attachment[] uploadFile(@ApiParam @RequestParam(value = "fileType", required = false) FileType fileType,
                                   @ApiParam @RequestParam("files") MultipartFile[] files) throws IOException {
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

    @ApiOperationSupport
    @ApiOperation(value = "下载文件(mongodb)", notes = "下载文件(mongodb)")
    @GetMapping("/file/{id}/mongodb")
    @Timed
    public void findToMongoDB(@PathVariable("id") String id,
                              @ApiIgnore HttpServletResponse response) throws IOException {
        attachmentService.findToMongoDB(id, response);
    }

    @ApiOperationSupport
    @ApiOperation(value = "下载文件(disk)", notes = "下载文件(disk)")
    @GetMapping("/file/{url}/disk")
    @Timed
    public void findToDisk(@PathVariable("url") String url,
                           @ApiIgnore HttpServletResponse response) {
        attachmentService.findToDisk(url, response);
    }

    @ApiOperationSupport
    @ApiOperation(value = "下载文件(database)", notes = "下载文件(database)")
    @GetMapping("/file/{id}/database")
    @Timed
    public void findToDataBase(@PathVariable("id") Long id,
                               @ApiIgnore HttpServletResponse response) {
        attachmentService.findToDataBase(id, response);
    }

    @ApiOperationSupport
    @ApiOperation(value = "下载文件(redis)", notes = "下载文件(redis)")
    @GetMapping("/file/{id}/redis")
    @Timed
    public void findToRedis(@PathVariable("id") String id,
                            @ApiIgnore HttpServletResponse response) {
        attachmentService.findToRedis(id, response);
    }

}
