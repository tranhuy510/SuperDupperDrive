package com.udacity.jwdnd.course1.cloudstorage.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {
    private Integer fileId;
    private String filename;
    private String contentType;
    private String fileSize;
    private Integer userId;
    private byte[] fileData;
}
