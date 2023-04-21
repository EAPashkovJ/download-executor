package com.eapashkov.downloadexecutor.controller;

import com.eapashkov.downloadexecutor.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        InputStream download = downloadService.download(fileId);
        Resource resource = new InputStreamResource(download);
        return ResponseEntity.ok()
//                .contentLength(gridFsFile.getLength())
//                .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
                .body(resource);


    }

}

