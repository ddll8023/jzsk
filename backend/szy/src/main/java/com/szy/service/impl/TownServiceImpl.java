package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Town;
import com.szy.mapper.TownMapper;
import com.szy.service.TownService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@DS("gcdd")
public class TownServiceImpl extends ServiceImpl<TownMapper, Town> implements TownService {
    @Resource
    private TownMapper townMapper;

    @Override
    public List<String> getAllNames() {
        return townMapper.getAllNames();
    }

    @Override
    public void create(Town town) {
        townMapper.insert(town);
    }

    @Override
    public List<Town> exportAll() {
        return townMapper.selectList(null);
    }
}
