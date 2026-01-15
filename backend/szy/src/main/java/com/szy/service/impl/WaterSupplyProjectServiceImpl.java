package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.WaterSupplyProject;
import com.szy.mapper.WaterSupplyProjectMapper;
import com.szy.service.WaterSupplyProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-14
 */
@Service
@DS("gcdd")
public class WaterSupplyProjectServiceImpl extends ServiceImpl<WaterSupplyProjectMapper, WaterSupplyProject> implements WaterSupplyProjectService {
    @Resource
    WaterSupplyProjectMapper waterSupplyProjectMapper;

    @Override
    public List<WaterSupplyProject> exportAll() {
        List<WaterSupplyProject> waterSupplyProjects = waterSupplyProjectMapper.selectList(null);
        return waterSupplyProjects;
    }
}
