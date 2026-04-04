package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.goros.habit_tracker.model.entity.File;
import org.goros.habit_tracker.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    @Value("${rustfs.bucket.name}")
    private String bucketName;
    public void createBucketIfNotExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        }
    }


    @SneakyThrows
    @Override
    public File uploadFile(MultipartFile file) {
        createBucketIfNotExists();

        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFileName);
        String contentType = file.getContentType();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v2/files/preview-file/" + fileName)
                .toUriString();
        return  File.builder().fileName(fileName)
                .fileUrl(fileUrl).fileType(file.getContentType()).fileSize(file.getSize()).build();
    }

    @Override
    public Resource getFileByFileName(String fileName) {

            InputStream inputStream = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build()
            );

        return new InputStreamResource(inputStream);
    }
}
