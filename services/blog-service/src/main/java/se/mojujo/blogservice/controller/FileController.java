package se.mojujo.blogservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.mojujo.blogservice.service.FileStorageService;

import java.util.Map;

@RestController
@RequestMapping("/files")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadMedia(
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {

        String url = fileStorageService.uploadFile(file, authentication);

        return ResponseEntity.ok(Map.of("url", url));
    }
}
