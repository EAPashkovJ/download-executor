package com.eapashkov.downloadexecutor.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("upload")
data class FileExchanger(

    @Id
    var id: String? = null,
    var filename: String? = null,
    var fileType: String? = null,
    var fileSize: String? = null,
    var metadata: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileExchanger

        if (id != other.id) return false
        if (filename != other.filename) return false
        if (fileType != other.fileType) return false
        if (fileSize != other.fileSize) return false
        return metadata.contentEquals(other.metadata)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + fileType.hashCode()
        result = 31 * result + fileSize.hashCode()
        result = 31 * result + metadata.contentHashCode()
        return result
    }

}