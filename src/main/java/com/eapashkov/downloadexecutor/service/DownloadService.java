package com.eapashkov.downloadexecutor.service;

import com.eapashkov.downloadexecutor.model.File;
import com.mongodb.client.gridfs.GridFSFindIterable;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface DownloadService {
    String upload(MultipartFile multipartFile) throws IOException;
    File download(String fileId) throws IOException;

}
