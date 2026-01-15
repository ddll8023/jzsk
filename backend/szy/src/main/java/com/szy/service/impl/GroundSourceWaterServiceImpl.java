package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.GroundSourceWater;
import com.szy.entity.Reservoir;
import com.szy.entity.Waterworks;
import com.szy.mapper.GroundSourceWaterMapper;
import com.szy.service.GroundSourceWaterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-03-21
 */
@Service
@DS("gcdd")
public class GroundSourceWaterServiceImpl extends ServiceImpl<GroundSourceWaterMapper, GroundSourceWater> implements GroundSourceWaterService {
    @Resource
    GroundSourceWaterMapper groundSourceWaterMapper;
    @Resource
    GroundSourceWaterService groundSourceWaterService;
    @Override
    @Transactional
    public void addGroundSourceWater(GroundSourceWater groundSourceWater) {
        String geom = "Point(" + groundSourceWater.getLongitude() + " " + groundSourceWater.getLatitude() + ")";
        groundSourceWaterService.save(groundSourceWater);
        groundSourceWaterMapper.addPosition(groundSourceWater.getId(), geom);
    }

    @Override
    @Transactional
    public void updateGroundSourceWater(GroundSourceWater groundSourceWater) {
        groundSourceWaterService.updateById(groundSourceWater);
        if(groundSourceWater.getLongitude() == null && groundSourceWater.getLatitude() == null) {
            return;
        }
        GroundSourceWater update = groundSourceWaterService.getById(groundSourceWater.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        Long id = groundSourceWater.getId();
        groundSourceWaterMapper.addPosition(id, geom);
    }

    @Override
    public List<GroundSourceWater> exportAll() {
        List<GroundSourceWater> groundSourceWaters = groundSourceWaterMapper.selectList(null);
        return groundSourceWaters;
    }
}
