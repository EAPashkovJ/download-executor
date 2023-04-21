package com.eapashkov.downloadexecutor.service.impl;

import com.eapashkov.downloadexecutor.model.File;
import com.eapashkov.downloadexecutor.repository.DownloadRepository;
import com.eapashkov.downloadexecutor.service.DownloadService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DownloadServiceImpl implements DownloadService {

    private final GridFSBucket gridFSBucket;
    private final DownloadRepository downloadRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

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
    public File download(String fileId) throws IOException {

        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));

        File file = new File();
        if(gridFSFile != null && gridFSFile.getMetadata() != null){
            file.setFilename(gridFSFile.getFilename());
            file.setContentType(gridFSFile.getMetadata().get("_contentType").toString());
            file.setChunkSize((Integer) gridFSFile.getMetadata().get("chunkSize"));
            file.setMetadata(IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));
        }

        return file;
    }
}