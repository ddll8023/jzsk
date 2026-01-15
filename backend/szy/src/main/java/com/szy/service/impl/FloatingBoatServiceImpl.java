package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.FloatingBoat;
import com.szy.mapper.FloatingBoatMapper;
import com.szy.service.FloatingBoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("gcdd")
public class FloatingBoatServiceImpl extends ServiceImpl<FloatingBoatMapper, FloatingBoat> implements FloatingBoatService {
    @Autowired
    private FloatingBoatMapper floatingBoatMapper;

    @Override
    public List<FloatingBoat> exportAll() {
        return floatingBoatMapper.selectList(null);
    }
}
