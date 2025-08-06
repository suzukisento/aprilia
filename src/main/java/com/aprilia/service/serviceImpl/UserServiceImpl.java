package com.aprilia.service.serviceImpl;

import com.aprilia.dao.User;
import com.aprilia.mapper.UserMapper;
import com.aprilia.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }
}
