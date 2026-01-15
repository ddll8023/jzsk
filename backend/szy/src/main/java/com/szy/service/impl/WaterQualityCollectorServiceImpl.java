package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.WaterLevelCollector;
import com.szy.entity.WaterQualityCollector;
import com.szy.mapper.WaterLevelCollectorMapper;
import com.szy.mapper.WaterQualityCollectorMapper;
import com.szy.service.WaterLevelCollectorService;
import com.szy.service.WaterQualityCollectorService;
import org.springframework.stereotype.Service;

@Service
@DS("eb_database")
public class WaterQualityCollectorServiceImpl extends ServiceImpl<WaterQualityCollectorMapper, WaterQualityCollector> implements WaterQualityCollectorService {

}
