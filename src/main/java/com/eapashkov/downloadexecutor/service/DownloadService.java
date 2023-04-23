package com.eapashkov.downloadexecutor.service;

import com.eapashkov.downloadexecutor.model.FileExchanger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DownloadService {
    String upload(MultipartFile multipartFile) throws IOException;
    FileExchanger download(String fileId) throws IOException;

}
