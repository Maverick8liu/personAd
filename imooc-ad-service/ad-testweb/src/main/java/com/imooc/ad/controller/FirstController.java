package com.imooc.ad.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FirstController {

    @GetMapping("firstBoot")
    public String firstBoot(){
        return "this is my firstBoot";
    }
}
