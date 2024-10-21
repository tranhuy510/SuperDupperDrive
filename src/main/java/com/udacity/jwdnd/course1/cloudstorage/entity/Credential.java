package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credential {
    private Integer credentialId;
    private String url;
    private String username;
    private String key;
    private String password;
    private Integer userId;
}
