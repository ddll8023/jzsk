package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Pump;
import com.szy.entity.PumpStation;

import java.util.List;

public interface PumpService extends IService<Pump> {
    List<Pump> exportAll();
}
