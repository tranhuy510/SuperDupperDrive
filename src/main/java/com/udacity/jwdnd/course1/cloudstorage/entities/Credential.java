package com.udacity.jwdnd.course1.cloudstorage.entities;

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
