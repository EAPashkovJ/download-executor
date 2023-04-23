package com.eapashkov.downloadexecutor.service.impl;

import com.eapashkov.downloadexecutor.model.FileExchanger;
import com.eapashkov.downloadexecutor.service.DownloadService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DownloadServiceImpl implements DownloadService {

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations gridFsOperations;

    @Override
    public List<String> upload(MultipartFile[] files) throws IOException {

        List<String> fileIds = new ArrayList<>();
        for (MultipartFile file : files) {
            DBObject metadata = new BasicDBObject();
            metadata.put("filesize", file.getSize());
            Object fileId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    metadata);
            fileIds.add(fileId.toString());

            log.info("{} has been uploaded!", StringUtils.capitalize(Objects.requireNonNull(file.getOriginalFilename())));
        }
        return fileIds;
    }


    @Override
    @SneakyThrows
    public FileExchanger download(String fileId) throws IOException {

        String cleanId = fileId.replace("\n", "");
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(cleanId))));

        FileExchanger fileExchanger = new FileExchanger();
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            fileExchanger.setFilename(gridFSFile.getFilename());
            fileExchanger.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            fileExchanger.setFileSize(gridFSFile.getMetadata().get("filesize").toString());
            fileExchanger.setMetadata(IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));

            log.info("{} has been downloaded!", StringUtils.capitalize(gridFSFile.getFilename()));
        }
        return fileExchanger;
    }
}