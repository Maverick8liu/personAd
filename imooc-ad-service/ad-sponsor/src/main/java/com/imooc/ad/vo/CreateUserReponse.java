package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserReponse {
    private Long userId;
    private String username;
    private String token;
    private Date createTime;
    private Date updateTime;
}
