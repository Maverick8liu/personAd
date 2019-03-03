package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable{

    private Integer code;
    private String message;
    private T data;

    public CommonResponse(Integer code,String message){
       log.info("****************code"+code+"***********"+message);
        this.code = code;
        this.message = message;
    }
}
