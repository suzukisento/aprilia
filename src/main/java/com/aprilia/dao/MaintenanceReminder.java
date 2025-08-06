package com.aprilia.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("maintenance_reminder")
public class MaintenanceReminder {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("vehicle_id")
    private Long vehicleId;
    @TableField("remind_at")
    private Date remindAt;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
