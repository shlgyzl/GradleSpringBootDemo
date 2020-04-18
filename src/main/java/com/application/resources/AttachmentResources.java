package com.application.resources;

import com.application.domain.enumeration.FileType;
import com.application.domain.jpa.Attachment;
import com.application.resources.exception.BusinessErrorException;
import com.application.service.AttachmentService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.application.domain.enumeration.BusinessErrorType.PARAMETER_EXCEPTION;

@RestController
@RequestMapping("/api")
public class AttachmentResources {
    private final AttachmentService attachmentService;

    public AttachmentResources(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileType", value = "文件类型", dataType = "FileType", paramType = "body", dataTypeClass = FileType.class),
            @ApiImplicitParam(name = "files", value = "文件数组", dataType = "MultipartFile", paramType = "body", dataTypeClass = MultipartFile.class)
    })
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiResponses({
            @ApiResponse(code = 200, message = "上传文件", response = Attachment.class, responseContainer = "List")
    })
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", dataType = "String", paramType = "body", dataTypeClass = String.class),
    })
    @ApiOperation(value = "下载文件", notes = "下载文件(mongodb)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "下载文件")
    })
    @GetMapping("/file/{id}/mongodb")
    @Timed
    public void findToMongoDB(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        attachmentService.findToMongoDB(id, response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "文件url", dataType = "String", paramType = "body", dataTypeClass = String.class),
    })
    @ApiOperation(value = "下载文件", notes = "下载文件(disk)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "下载文件")
    })
    //@GetMapping("/file/{url}/disk")
    @Timed
    public void findToDisk(@PathVariable("url") String url, HttpServletResponse response) throws IOException {
        attachmentService.findToDisk(url, response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", dataType = "String", paramType = "body", dataTypeClass = String.class),
    })
    @ApiOperation(value = "下载文件", notes = "下载文件(databse)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "下载文件")
    })
    @GetMapping("/file/{id}/database")
    @Timed
    public void findToDataBase(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        attachmentService.findToDataBase(id, response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", dataType = "String", paramType = "body", dataTypeClass = String.class),
    })
    @ApiOperation(value = "下载文件", notes = "下载文件(redis)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "下载文件")
    })
    @GetMapping("/file/{id}/redis")
    @Timed
    public void findToRedis(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        attachmentService.findToRedis(id, response);
    }

}
