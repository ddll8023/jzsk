package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Herb;
import com.szy.mapper.HerbMapper;
import com.szy.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("gcdd")
public class HerbServiceImpl extends ServiceImpl<HerbMapper, Herb> implements HerbService {
    @Autowired
    private HerbMapper herbMapper;
    @Override
    public List<Herb> exportAll() {
        return herbMapper.selectList(null);
    }
}
