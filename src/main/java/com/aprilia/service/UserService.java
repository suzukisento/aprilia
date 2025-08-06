package com.aprilia.service;

import com.aprilia.dao.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User findByUsername(String username);
}
