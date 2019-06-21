package com.byc.controller;

import com.byc.dao.entity.Users;
import com.byc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by baiyc
 * 2019/5/6/006 17:52
 * Description：
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/reg")
    public String regist(Users user){
        userService.save(user);
        return "success";
    }
}
