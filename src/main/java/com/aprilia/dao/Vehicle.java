package com.aprilia.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("vehicle")
public class Vehicle {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Integer mileage;
    @TableField("last_oil_change_date")
    private Date lastOilChangedate;
    @TableField("last_oil_km")
    private Integer lastOilKm;
    @TableField("oil_brand")
    private String oilBrand;
    private String remark;
    @TableField("owner_id")
    private Long ownerId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
