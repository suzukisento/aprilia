package com.aprilia.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String role;//admin/repair/user
    @TableField("engine_no")
    private String engineNo;
    @TableField("frame_no")
    private String frameNo;
    @TableField("car_photo_url")
    private String carPhotoUrl;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
