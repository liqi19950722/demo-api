package com.acme.biz.web.controller;

import com.acme.biz.api.entity.User;
import com.acme.biz.api.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @PostMapping(value = "/register/v3")
    public Boolean register(User user) {
        return Boolean.TRUE;
    }
}
