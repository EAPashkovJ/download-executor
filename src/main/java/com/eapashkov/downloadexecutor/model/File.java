package com.eapashkov.downloadexecutor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collation = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    private String id;
    private String filename;
    private String contentType;
    private long length;
    private int chunkSize;
    private Date uploadDate;
    private String md5;
    private Object metadata;

}