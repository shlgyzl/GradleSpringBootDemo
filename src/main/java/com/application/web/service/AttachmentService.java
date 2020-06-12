package com.application.web.service;

import com.application.web.domain.jpa.Attachment;
import com.application.web.properties.FileApplicationProperties;
import com.application.web.repository.jpa.AttachmentRepository;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
public class AttachmentService {
    public static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpg", "image/jpeg", "image/png");
    private static final String CONTENT_TYPE_FIELD = "_contentType";
    private final FileApplicationProperties fileApplicationProperties;
    private final AttachmentRepository attachmentRepository;
    private final GridFsTemplate gridFSTemplate;
    private final GridFSBucket gridFSBucket;


    public AttachmentService(FileApplicationProperties fileApplicationProperties, AttachmentRepository attachmentRepository, GridFsTemplate gridFSTemplate, GridFSBucket gridFSBucket) {
        this.fileApplicationProperties = fileApplicationProperties;
        this.attachmentRepository = attachmentRepository;
        this.gridFSTemplate = gridFSTemplate;
        this.gridFSBucket = gridFSBucket;
    }

    public Attachment[] saveToDisk(MultipartFile[] files) throws IOException {
        List<Attachment> result = new ArrayList<>(10);
        for (MultipartFile multipartFile : files) {
            // 随机文件名
            long timeMillis = System.currentTimeMillis();
            String fileName = timeMillis + getSuffix(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Path filePath = Paths.get(getSystemPath(), getFileParent(multipartFile), fileName);
            Files.createDirectories(filePath.getParent());
            log.info("上传文件成功");
            if (log.isDebugEnabled()) {
                log.debug("文件根目录为:[{}],文件目录为:[{}],文件路径为:[{}],剩余可用空间为:[{}]",
                        filePath.getRoot(), filePath.getParent(), filePath.getFileName(),
                        Files.getFileStore(filePath.getRoot()).getTotalSpace());
            }
            Files.copy(multipartFile.getInputStream(), filePath, REPLACE_EXISTING);
            Attachment attachment = getAttachment(multipartFile, fileName, filePath, timeMillis);
            result.add(attachment);
        }
        return result.toArray(new Attachment[0]);

       /* List<String> result = new ArrayList<>(10);
        for (MultipartFile multipartFile : files) {
            // 随机文件名
            String fileName = getRandomFileName(multipartFile);
            Path filePath = Paths.get(getSystemPath(), fileName);
            Files.createDirectories(filePath.getParent());
            log.info("上传文件成功");
            if (log.isDebugEnabled()) {
                log.debug("文件根目录为:[{}],文件目录为:[{}],文件路径为:[{}],剩余可用空间为:[{}]",
                        filePath.getRoot(), filePath.getParent(), filePath.getFileName(),
                        Files.getFileStore(filePath.getRoot()).getTotalSpace());
            }
            multipartFile.transferTo(filePath.toFile());
            result.add(fileName);
        }
        return result;*/
    }


    public Attachment[] saveToDataBase(MultipartFile[] files) throws IOException {
        List<Attachment> result = new ArrayList<>(10);
        for (MultipartFile multipartFile : files) {
            // 随机文件名
            long timeMillis = System.currentTimeMillis();
            String fileName = timeMillis + getSuffix(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Path filePath = Paths.get(getSystemPath(), getFileParent(multipartFile), fileName);
            result.add(attachmentRepository.save(getAttachment(multipartFile, fileName, filePath, timeMillis)));
        }
        return result.toArray(new Attachment[0]);
    }


    public List<String> saveToRedis(MultipartFile[] files) throws IOException {
        return null;
    }

    public Attachment[] saveToMongoDB(MultipartFile[] files) throws IOException {
        List<Attachment> result = new ArrayList<>(10);
        for (MultipartFile multipartFile : files) {
            long timeMillis = System.currentTimeMillis();
            // 随机文件名
            String fileName = timeMillis + getSuffix(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            ObjectId store = gridFSTemplate.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                    multipartFile.getContentType());
            Path filePath = Paths.get(getSystemPath(), getFileParent(multipartFile), fileName);
            Attachment attachment = getAttachment(multipartFile, store.toString(), filePath, timeMillis);
            result.add(attachment);
        }
        return result.toArray(new Attachment[0]);
    }

    public void findToDisk(String url, HttpServletResponse response) {

    }

    public void findToDataBase(Long id, HttpServletResponse response) {

    }

    public void findToMongoDB(String id, HttpServletResponse response) throws IOException {
        GridFSFile fsFile = gridFSTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (fsFile == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "无法找到该文件：" + id);
            return;
        }
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(fsFile, gridFSDownloadStream);
        response.setContentType(Objects.requireNonNull(fsFile.getMetadata()).getString(CONTENT_TYPE_FIELD));
        response.setHeader("Content-Disposition", "attachment;filename="
                + fsFile.getFilename() + ";filename*=utf-8''" + URLEncoder.encode(fsFile.getFilename(), "UTF-8"));
        response.setHeader("cache-control", "max-age=3600,public");
        response.setHeader("pragma", "cache");
        IOUtils.copy(gridFsResource.getInputStream(), response.getOutputStream());
    }

    public void findToRedis(String id, HttpServletResponse response) {

    }

    private Attachment getAttachment(MultipartFile multipartFile, String fileName, Path filePath, long timeMillis) throws IOException {
        Attachment attachment = new Attachment()
                .setContentType(multipartFile.getContentType())
                .setOriginalFileName(multipartFile.getOriginalFilename())
                .setFileSize(multipartFile.getSize())
                .setFilePath(filePath.toString())
                .setFileSizeFormatted(getPrintSize(multipartFile.getSize()))
                .setFileName(fileName)
                .setSimpleName(timeMillis + "")
                //.setContent(multipartFile.getBytes())
                .setContent(Files.readAllBytes(filePath))
                .setStringContent(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
                .setFile(filePath.toFile());// 用于访问文件
        return attachment.setUrl(getUrl(attachment));
    }

    private Path saveFile(MultipartFile multipartFile, String fileName) throws IOException {
        Path filePath = Paths.get(getSystemPath(), getFileParent(multipartFile), fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(multipartFile.getInputStream(), filePath, REPLACE_EXISTING);
        return filePath;
    }

    public String getSystemPath() {
        return System.getProperty("user.dir") + fileApplicationProperties.getPath();
    }

    private String getUrl(Attachment attachment) {
        return attachment.getContentType()
                + "/" + attachment.getSimpleName()
                + getSuffix(attachment.getOriginalFileName());
    }

    private String getSuffix(String originalFileName) {
        return originalFileName.substring(originalFileName.indexOf("."));
    }

    private String getSimpleName() {
        return System.currentTimeMillis() + "";
    }

    private String getFileParent(MultipartFile multipartFile) {// 按照文件类型创建文件夹
        return multipartFile.getContentType();
    }

    private static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return size + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return size / 100 + "."
                    + size % 100 + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return size / 100 + "."
                    + size % 100 + "GB";
        }
    }
}
