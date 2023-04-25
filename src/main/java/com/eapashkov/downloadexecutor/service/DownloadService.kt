package com.eapashkov.downloadexecutor.service

import com.eapashkov.downloadexecutor.model.FileExchanger
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface DownloadService {
    @Throws(IOException::class)
    fun upload(files: Array<MultipartFile?>?): List<String?>?

    @Throws(IOException::class)
    fun download(fileId: String?): FileExchanger?
}
