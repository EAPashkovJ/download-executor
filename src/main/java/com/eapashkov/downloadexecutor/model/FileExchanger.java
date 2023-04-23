package com.eapashkov.downloadexecutor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileExchanger {

    @Id
    private String id;
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] metadata;


}