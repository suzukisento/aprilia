package com.aprilia.controller;

import com.aprilia.dao.User;
import com.aprilia.dao.Vehicle;
import com.aprilia.service.UserService;
import com.aprilia.service.VehicleService;
import com.aprilia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/admin/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BCryptPasswordEncoder encoder;

//    查询所有用户信息
    @GetMapping("/list")
    public List<User> list(HttpServletRequest request){
        if (!"admin".equals(jwtUtil.getUserRoleFromRequest(request))){
            throw new RuntimeException("权限不足");
        }
        return userService.list();
    }
//    根据用户名查询用户信息
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username){
        return userService.findByUsername( username);
    }
    @PostMapping
    public boolean addUser(@RequestBody User user){
        if (!"admin".equals(user.getRole())){
            throw new RuntimeException("权限不足");
        }
        return userService.save(user);
    }
//    更新用户信息
    @PutMapping
    public boolean updateUser(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        if ("user".equals(user.getRole())){
            throw new RuntimeException("权限不足");
        }
        return userService.updateById(user);
    }
//    用于修改个人密码
    @PutMapping("/updatepasswd")
    public boolean upDateUser(@RequestBody User user ,HttpServletRequest  request){
        Long userid = jwtUtil.getUserIdFromRequest(request);
        String username = jwtUtil.getUsernameFromRequest(request);
        String role = jwtUtil.getUserRoleFromRequest(request);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(role);
        user.setUsername(username);
        user.setId(userid);
        return userService.updateById(user);
    }
//    删除特定用户信息
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id,HttpServletRequest request){
        List<Vehicle> origin = vehicleService.list();
        User user = userService.getById(id);
        String role = jwtUtil.getUserRoleFromRequest(request);
        if ("admin".equals(role)){
            for (Vehicle v : origin){
                if (v.getOwnerId().equals(id)){
                    throw new RuntimeException("用户有车辆，请先注销车辆");
                }
            }
            if (user == null) throw new RuntimeException("用户不存在");
            return userService.removeById(id);
        }
        throw new RuntimeException("权限不足");

    }

}
