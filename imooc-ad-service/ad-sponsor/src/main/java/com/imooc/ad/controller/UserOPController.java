package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IUserService;
import com.imooc.ad.vo.CreateUserReponse;
import com.imooc.ad.vo.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserOPController {

    private final IUserService userService;

    @Autowired
    public UserOPController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public CreateUserReponse createUser(@RequestBody CreateUserRequest request) throws AdException{
        log.info("ad-sponsor:Create User ->{}", JSON.toJSONString(request));
        return userService.createUser(request);
    }

    @GetMapping("/testUrl")
    public String testUrl(){
        return "this is my firstBoot";
    }
}
