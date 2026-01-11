package se.mojujo.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${SUPABASE_ENDPOINT}")
    String s3Endpoint;

    @Value("${SUPABASE_ACCESS_KEY}")
    String supabaseAccessKey;

    @Value("${SUPABASE_SECRET_KEY}")
    String supabaseSecretKey;

    @Bean
    public S3Client s3Client() {
        System.out.println("Configuring S3 with endpoint: [" + s3Endpoint + "]");


        AwsBasicCredentials credentials = AwsBasicCredentials.create(supabaseAccessKey, supabaseSecretKey);
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(s3Endpoint.trim()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}
