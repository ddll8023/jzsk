package com.szy.service.impl;

import com.szy.entity.StRiversR;
import com.szy.mapper.StRiversRMapper;
import com.szy.service.IStRiversRService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 河道水情表 服务实现类
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Service
@DS("dbo")
public class StRiversRServiceImpl extends ServiceImpl<StRiversRMapper, StRiversR> implements IStRiversRService {

    @Override
    public Page<StRiversR> listByPage(Integer page, Integer size) {
        Page<StRiversR> pageObj = new Page<>(page, size);
        QueryWrapper<StRiversR> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("tm"); // 按时间倒序排列
        return this.page(pageObj, queryWrapper);
    }
}