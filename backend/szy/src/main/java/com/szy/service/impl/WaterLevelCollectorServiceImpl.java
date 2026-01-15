package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.WaterLevelCollector;
import com.szy.mapper.WaterLevelCollectorMapper;
import com.szy.service.WaterLevelCollectorService;
import org.springframework.stereotype.Service;

@Service
@DS("collector")
public class WaterLevelCollectorServiceImpl extends ServiceImpl<WaterLevelCollectorMapper, WaterLevelCollector> implements WaterLevelCollectorService {

}
