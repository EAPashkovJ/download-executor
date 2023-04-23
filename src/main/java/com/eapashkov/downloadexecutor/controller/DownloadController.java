package com.eapashkov.downloadexecutor.controller;

import com.eapashkov.downloadexecutor.model.FileExchanger;
import com.eapashkov.downloadexecutor.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")

public class DownloadController {

    private final DownloadService downloadService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        return new ResponseEntity<>(downloadService.upload(files), HttpStatus.CREATED);
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {

        FileExchanger fileExchanger = downloadService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileExchanger.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileExchanger.getFilename() + "\"")
                .body(new ByteArrayResource(fileExchanger.getMetadata()));
    }

}

