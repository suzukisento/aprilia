package com.aprilia.controller;

import com.aprilia.dao.User;
import com.aprilia.dao.Vehicle;
import com.aprilia.service.UserService;
import com.aprilia.service.VehicleService;
import com.aprilia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

//    获取所有车辆信息
    @GetMapping("/list")
    public List<Vehicle> list(HttpServletRequest request){
        String role = jwtUtil.getUserRoleFromRequest(request);
        Long  userId = jwtUtil.getUserIdFromRequest(request);
//        非管理员及资质用户无法查看所有车辆信息（待验证）
        if ("admin".equals(role) || "vip".equals(role)){
            return vehicleService.list();
        }else {
            return vehicleService.lambdaQuery()
                    .eq(Vehicle::getOwnerId,userId)
                    .list();
        }

    }
    /**
     * 后期添加功能：通过ownerId查询用户车辆（未调整）
     * 提供给admin和repaip的插叙车辆页面
     */
    @PostMapping("/ownerid")
    public List<Vehicle> getByOwnerId(@RequestBody User user , HttpServletRequest request){
        String ownerId = user.getId().toString();
        String role = jwtUtil.getUserRoleFromRequest(request);
        if("admin".equals( role) || "vip".equals(role)){
            return vehicleService.lambdaQuery()
                    .eq(Vehicle::getOwnerId,ownerId)
                    .list();
        }
        throw new RuntimeException("无权限查询");

    }
//    根据id获取车辆信息

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable Long id,HttpServletRequest request){
        Vehicle  v = vehicleService.getById(id);
        String role = jwtUtil.getUserRoleFromRequest(request);
        Long userId = jwtUtil.getUserIdFromRequest(request);
        if (v == null) return null;
        if("admin".equals(role) || "vip".equals(role) || v.getOwnerId().equals(userId)){
            return v;
        }
        throw new RuntimeException("无权限");
    }
//    添加车辆信息
    @PostMapping
    public boolean addVehicle(@RequestBody Vehicle vehicle,HttpServletRequest request){
        String role = jwtUtil.getUserRoleFromRequest(request);
//        非管理员及资质用户无法添加车辆信息（未修改）
        if (!"admin".equals(role)){
//            只能修改个人的车辆信息
            /*
            该方法打开则可使user车主对自己车辆信息进行修改，是一个 bug
            vehicle.setOwnerId(userId);
            */
            throw new RuntimeException("权限不足");
        }
        return vehicleService.save(vehicle);
    }
//    修改车辆信息
    /*
    该方法有个bug：非管理员用户得到管理员token的话，可以通过编写json文件绕过前端页面修改车辆信息
    解决方案：管理员用户保护好自身账户信息
            加钱修改后端逻辑，只接受需要修改信息，并创建对象存储修改信息交由service层处理
    */
    @PutMapping
    public boolean update(@RequestBody Vehicle vehicle,HttpServletRequest request){
        String role = jwtUtil.getUserRoleFromRequest(request);
        Long userId = jwtUtil.getUserIdFromRequest(request);
        Vehicle origin = vehicleService.getById(vehicle.getId());
        if (origin == null) throw new RuntimeException("车辆不存在");
        if ("admin".equals(role) || "vip".equals( role)){
            return vehicleService.updateById(vehicle);
        }
        throw new RuntimeException("无修改权限");
    }
//    删除车辆信息
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id,HttpServletRequest request){
        String role = jwtUtil.getUserRoleFromRequest(request);
        Long userId = jwtUtil.getUserIdFromRequest(request);
        Vehicle v = vehicleService.getById(id);
        if (v == null) throw new RuntimeException("车辆不存在");
/*
 * 删除车辆信息(该版本能让用户删除自己的车）
        if ("admin".equals(role) || v.getOwnerId().equals(userId)){
            return vehicleService.removeById(id);
        }
        */


        /*
       非管理员及资质用户无法删除车辆信息（未修改）*/
        if ("admin".equals(role)){
            return vehicleService.removeById(id);
        }
        throw new RuntimeException("无删除权限");
    }
}
