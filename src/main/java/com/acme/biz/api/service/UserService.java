package com.acme.biz.api.service;

import com.acme.biz.api.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */

public interface UserService {

    Boolean register(@RequestBody User user);
}
