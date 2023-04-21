package com.eapashkov.downloadexecutor.service;

import com.mongodb.client.gridfs.GridFSFindIterable;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface DownloadService {
   // public String upload(InputStream inputStream, String fileName, String contentType);
    public InputStream download(ObjectId objectId);
    public String upload(MultipartFile multipartFile) throws IOException;

}
