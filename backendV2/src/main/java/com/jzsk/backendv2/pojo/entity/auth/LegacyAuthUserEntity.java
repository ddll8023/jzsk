package com.jzsk.backendv2.pojo.entity.auth;

import lombok.Data;

@Data
public class LegacyAuthUserEntity {

    private Long id;
    private String username;
    private String password;
    private String name;
}
