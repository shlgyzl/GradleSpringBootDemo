package com.application.service;

import com.application.domain.jpa.Attachment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentService {

    public Attachment[] save(MultipartFile[] files) {
        return null;
    }
}
