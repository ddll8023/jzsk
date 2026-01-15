package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Pump;
import com.szy.entity.PumpStation;
import com.szy.mapper.PumpMapper;
import com.szy.service.PumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("gcdd")
public class PumpServiceImpl extends ServiceImpl<PumpMapper, Pump> implements PumpService {
    @Autowired
    private PumpMapper pumpMapper;
    @Override
    public List<Pump> exportAll() {
        List<Pump> pumps = pumpMapper.selectList(null);
        return pumps;
    }
}
