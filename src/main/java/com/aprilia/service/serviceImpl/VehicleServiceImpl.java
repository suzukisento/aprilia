package com.aprilia.service.serviceImpl;

import com.aprilia.dao.Vehicle;
import com.aprilia.mapper.VehicleMapper;
import com.aprilia.service.VehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {
}
