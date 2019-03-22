package com.socket.lgy.controller;

import com.socket.lgy.socket.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.View;

@Slf4j
@Controller
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    SocketUtil socketUtil;

    @RequestMapping(value="/get",method = RequestMethod.GET)
    public String get(Model model){
        return "get";
    }


    @RequestMapping(value = "getResult",method = RequestMethod.POST)
    public String getResult(Model model, HttpServletRequest request){
       String modelType =  request.getParameter("modelType");
       log.warn("UserController getResult is:{}",modelType);

       model.addAttribute("result","hello boot");

       return "result";
    }

}
