package com.eapashkov.downloadexecutor.service;

import com.eapashkov.downloadexecutor.model.FileExchanger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DownloadService {
    List<String> upload(MultipartFile[] files) throws IOException;

    FileExchanger download(String fileId) throws IOException;

}
