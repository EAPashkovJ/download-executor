package com.eapashkov.downloadexecutor.repository

import com.eapashkov.downloadexecutor.model.FileExchanger
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DownloadRepository : MongoRepository<FileExchanger?, String?>
