package com.szy.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.controller.RiverCollectorController;
import com.szy.entity.RiverCollector;

@DS("collector")
public interface RiverCollectorService extends IService<RiverCollector> {

    void syncFlowData();

}
