package com.eapashkov.downloadexecutor.service.impl

import com.eapashkov.downloadexecutor.model.FileExchanger
import com.eapashkov.downloadexecutor.service.DownloadService
import com.mongodb.BasicDBObject
import lombok.SneakyThrows
import mu.KotlinLogging
import org.apache.commons.io.IOUtils
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
@Transactional
class DownloadServiceImpl(private val gridFsTemplate: GridFsTemplate, private val gridFsOperations: GridFsOperations) :
    DownloadService {

    private val logger = KotlinLogging.logger {}

    @Throws(IOException::class)
    override fun upload(files: Array<MultipartFile?>?): List<String?>? {
        val fileIds = ArrayList<String>()
        if (files != null) {
            for (file in files) {
                val metadata = BasicDBObject()
                metadata["filesize"] = file?.size
                val fileId = gridFsTemplate?.store(
                    file?.inputStream!!,
                    file.originalFilename,
                    file.contentType,
                    metadata
                )
                fileIds.add(fileId.toString())
                logger.info("{} has been uploaded!", StringUtils.capitalize(file?.originalFilename!!))
            }
        }
        return fileIds
    }

    @SneakyThrows
    override fun download(fileId: String?): FileExchanger? {
        val cleanId = fileId?.replace("\n", "")
        val gridFSFile = gridFsTemplate!!.findOne(Query(Criteria.where("_id").`is`(ObjectId(cleanId))))
        val fileExchanger = FileExchanger()
        if (gridFSFile.metadata != null) {
            fileExchanger.filename = gridFSFile.filename
            fileExchanger.fileType = gridFSFile.metadata?.get("_contentType").toString()
            fileExchanger.fileSize = gridFSFile.metadata?.get("filesize").toString()
            fileExchanger.metadata = IOUtils.toByteArray(gridFsOperations!!.getResource(gridFSFile).inputStream)
            logger.info("{} has been downloaded!", StringUtils.capitalize(gridFSFile.filename))
        }
        return fileExchanger
    }
}