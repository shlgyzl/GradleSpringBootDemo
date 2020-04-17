package com.application.resources;

import com.application.domain.enumeration.FileType;
import com.application.domain.jpa.Attachment;
import com.application.resources.exception.BusinessErrorException;
import com.application.service.AttachmentService;
import io.micrometer.core.annotation.Timed;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.application.domain.enumeration.BusinessErrorType.PARAMETER_EXCEPTION;

@RestController
@RequestMapping("/api")
public class AttachmentResources {
    private final AttachmentService attachmentService;

    public AttachmentResources(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/files/upload")
    @Timed
    public Attachment[] uploadFile(@RequestParam("fileType") FileType fileType,
                                   @RequestParam("files") MultipartFile[] files) {
        if (ObjectUtils.isEmpty(files)) {
            throw new BusinessErrorException(PARAMETER_EXCEPTION);
        }
        return attachmentService.save(files);
    }
}
