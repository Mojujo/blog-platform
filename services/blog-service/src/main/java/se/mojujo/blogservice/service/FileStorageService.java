package se.mojujo.blogservice.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.mojujo.blogservice.post.AuthenticatedUserDetails;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
public class FileStorageService {

    private final S3Client s3Client;
    private final String bucket;
    private final String publicBaseUrl;

    public FileStorageService(
            @Value("${SUPABASE_ENDPOINT}") String s3Endpoint,
            @Value("${SUPABASE_URL}") String supabaseUrl,
            @Value("${SUPABASE_KEY}") String supabaseKey,
            @Value("${SUPABASE_BUCKET}") String bucket) {

        this.bucket = bucket;
        this.publicBaseUrl = supabaseUrl + "/storage/v1/object/public/" + bucket;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(supabaseKey, supabaseKey);

        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(s3Endpoint))
                .region(Region.EU_NORTH_1)
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();

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
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return publicBaseUrl + "/" + key;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e); // TODO CUSTOM EXCEPTION
        }
    }
}
