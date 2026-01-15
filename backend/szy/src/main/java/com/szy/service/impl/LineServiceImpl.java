package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Line;
import com.szy.entity.WarningInformation;
import com.szy.mapper.LineMapper;
import com.szy.mapper.WarningInformationMapper;
import com.szy.service.LineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@DS("gcdd")
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements LineService {
    @Resource
    LineMapper lineMapper;

    @Override
    public List<Line> exportAll() {
        return lineMapper.selectList(null);
    }

    @Override
    public boolean existsByName(String name) {
        QueryWrapper<Line> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name); // 假设字段名为name，并且是唯一的
        return lineMapper.selectCount(queryWrapper) > 0;
    }
}
