package com.eapashkov.downloadexecutor.service.impl;

import com.eapashkov.downloadexecutor.model.FileExchanger;
import com.eapashkov.downloadexecutor.service.DownloadService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DownloadServiceImpl implements DownloadService {

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


        log.info("{} has been uploaded!", StringUtils.capitalize(multipartFile.getOriginalFilename()));
        return fileId.toString();
    }


    @Override
    public FileExchanger download(String fileId) throws IOException {

        String cleanId = fileId.replace("\n", "");
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(cleanId))));

        FileExchanger fileExchanger = new FileExchanger();
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            fileExchanger.setFilename(gridFSFile.getFilename());
            fileExchanger.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            fileExchanger.setFileSize(gridFSFile.getMetadata().get("filesize").toString());
            fileExchanger.setMetadata(IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));

            log.info("{} hax been downloaded!", StringUtils.capitalize(gridFSFile.getFilename()));
        }
        return fileExchanger;
    }
}