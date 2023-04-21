package com.eapashkov.downloadexecutor.repository;

import com.eapashkov.downloadexecutor.model.File;
import com.mongodb.client.gridfs.GridFSFindIterable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadRepository extends MongoRepository<File, String> {

}
