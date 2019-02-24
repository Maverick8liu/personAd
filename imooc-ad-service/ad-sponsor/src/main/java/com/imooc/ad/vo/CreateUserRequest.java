package com.imooc.ad.vo;

import org.apache.commons.lang.StringUtils;

public class CreateUserRequest {
    private String username;

    public boolean validate() {

        return !StringUtils.isEmpty(username);
    }

    public String getUsername() {
        return username;
    }
}
