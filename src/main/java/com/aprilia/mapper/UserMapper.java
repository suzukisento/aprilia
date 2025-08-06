package com.aprilia.mapper;

import com.aprilia.dao.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper extends BaseMapper<User> {


}
