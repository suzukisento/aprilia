package com.aprilia.controller;

import com.aprilia.dao.User;
import com.aprilia.service.UserService;
import com.aprilia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService  userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody User user){
        Map<String,Object> result = new HashMap<>();
        if(userService.findByUsername(user.getUsername()) != null){
            result.put("code",400);
            result.put("msg","用户已存在");
            return result;
        }
//        设置特殊用户修改部分
        user.setRole("user");
//        分为：admin、repaip、user
        user.setPassword(encoder.encode(user.getPassword()));
        boolean saved = userService.save(user);
        result.put("code",saved?200:400);
        result.put("msg",saved?"注册成功":"注册失败");
        return result;
    }
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody User user){
        Map<String,Object> result = new HashMap<>();
        User dbUser = userService.findByUsername(user.getUsername());
        if(dbUser == null || !encoder.matches(user.getPassword(),dbUser.getPassword())){
            result.put("code",401);
            result.put("msg","用户名或密码错误");
            return result;
        }
        String token = jwtUtil.generateToken(dbUser.getId(), user.getUsername(),dbUser.getRole());
        result.put("code",200);
        result.put("msg","登录成功");
        result.put("token",token);
        result.put("role",dbUser.getRole());
        return result;
    }

}
