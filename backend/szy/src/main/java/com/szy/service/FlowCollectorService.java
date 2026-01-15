package com.szy.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.FlowCollector;

@DS("collector")
public interface FlowCollectorService extends IService<FlowCollector> {
}
