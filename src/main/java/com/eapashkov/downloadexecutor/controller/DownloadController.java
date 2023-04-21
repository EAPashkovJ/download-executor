package com.eapashkov.downloadexecutor.controller;

import com.eapashkov.downloadexecutor.model.File;
import com.eapashkov.downloadexecutor.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")

public class DownloadController {

    private final DownloadService downloadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = downloadService.upload(file);
            return ResponseEntity.ok(fileId);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        File download = downloadService.download(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download.getFilename())
                .body(new ByteArrayResource(download.getMetadata()));



    }

}

