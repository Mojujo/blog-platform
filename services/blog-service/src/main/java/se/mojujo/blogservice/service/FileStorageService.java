package se.mojujo.blogservice.service;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.mojujo.blogservice.exception.FileStorageException;
import se.mojujo.blogservice.post.AuthenticatedUserDetails;
import se.mojujo.blogservice.util.LogUtil;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final S3Client s3Client;
    private final String bucket;
    private final String publicBaseUrl;

    public FileStorageService(
            S3Client s3Client,
            @Value("${SUPABASE_URL}") String supabaseUrl,
            @Value("${SUPABASE_BUCKET}") String bucket) {

        this.s3Client = s3Client;
        this.bucket = bucket;
        this.publicBaseUrl = supabaseUrl + "/storage/v1/object/public/" + bucket;
    }

    public String uploadFile(MultipartFile file, Authentication authentication) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "." + extension;
            String key = user.getUserId() + "/" + fileName;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

            LogUtil.info(logger,
                    "IMAGE_UPLOADED",
                    "Image was successfully uploaded",
                    "url", publicBaseUrl + "/" + key);

            return publicBaseUrl + "/" + key;


        } catch (S3Exception | IOException e) {

            LogUtil.info(logger,
                    "IMAGE_UPLOAD_FAILED",
                    "Image upload has encountered an error");
            throw new FileStorageException("Failed to upload media");
        }
    }
}
