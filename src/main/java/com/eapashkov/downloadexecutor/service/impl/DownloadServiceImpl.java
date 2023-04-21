package com.eapashkov.downloadexecutor.service.impl;

import com.eapashkov.downloadexecutor.repository.DownloadRepository;
import com.eapashkov.downloadexecutor.service.DownloadService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoGridFSException;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DownloadServiceImpl implements DownloadService {

    private final GridFSBucket gridFSBucket;
    private final DownloadRepository downloadRepository;
    private final GridFsTemplate gridFsTemplate;

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("filesize", multipartFile.getSize());
        Object fileId = gridFsTemplate.store(
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                metadata);

        log.info("{} file has been uploaded", multipartFile.getOriginalFilename());
        return fileId.toString();
    }


    @Override
    public InputStream download(ObjectId objectId) {

        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(objectId);
        int fileLength = (int) downloadStream.getGridFSFile().getLength();
        byte[] bytesToWriteTo = new byte[fileLength];
        try {
            downloadStream.read(bytesToWriteTo);
        } catch (Exception e) {
            throw new MongoGridFSException("Error downloading file", e);
        }
        downloadStream.close();
        log.info("{} file has been downloaded", objectId);
        return downloadStream;

    }
}