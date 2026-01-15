package com.szy.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.WaterLevel;
import com.szy.entity.WaterLevelCollector;

@DS("collector")
public interface WaterLevelCollectorService extends IService<WaterLevelCollector> {
}
