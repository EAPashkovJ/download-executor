package com.eapashkov.downloadexecutor.controller

import com.eapashkov.downloadexecutor.service.DownloadService
import lombok.RequiredArgsConstructor
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
class DownloadController(private val downloadService: DownloadService) {

    @PostMapping("/upload")
    @Throws(IOException::class)
    fun uploadFiles(@RequestParam("files") files: Array<MultipartFile?>?): ResponseEntity<List<String?>?> {
        return ResponseEntity(downloadService.upload(files), HttpStatus.CREATED)
    }

    @GetMapping("/download/{id}")
    @Throws(IOException::class)
    fun download(@PathVariable id: String?): ResponseEntity<ByteArrayResource>? {
        val fileExchanger = downloadService.download(id)

        if (fileExchanger != null) {
            fileExchanger.fileType?.let { MediaType.parseMediaType(it) }?.let {

                return ResponseEntity.ok()
                    .contentType(it)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileExchanger.filename + "\"")
                    .body(fileExchanger.metadata?.let { it1 -> ByteArrayResource(it1) })
            }
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}