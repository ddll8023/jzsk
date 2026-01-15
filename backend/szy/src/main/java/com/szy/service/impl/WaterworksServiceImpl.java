package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.Waterworks;
import com.szy.mapper.WaterworksMapper;
import com.szy.service.WaterworksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-24
 */
@Service
@DS("gcdd")
public class WaterworksServiceImpl extends ServiceImpl<WaterworksMapper, Waterworks> implements WaterworksService {
    @Resource
    WaterworksMapper waterworksMapper;
    @Resource
    WaterworksService waterworksService;

    @Override
    @Transactional
    public void addWaterworks(Waterworks waterworks) {
        Date dateNow = new Date();
        String geom = "Point(" + waterworks.getLongitude() + " " + waterworks.getLatitude() + ")";
        waterworksMapper.add(waterworks.getWaterSupply(), waterworks.getCode(), waterworks.getName(), waterworks.getAddress(), waterworks.getManageUnit(), waterworks.getLongitude(), waterworks.getLatitude(), waterworks.getDesignScale(), waterworks.getWaterSupplyRange(), waterworks.getWaterSupplyLoadRate(), waterworks.getPopulation(), waterworks.getResponsiblePerson(), waterworks.getPhone(), waterworks.getDate(), dateNow, dateNow, geom);
    }

    //10
    @Override
    @Transactional
    public void updateWaterworks(Waterworks waterworks) {
        waterworksService.updateById(waterworks);
        if(waterworks.getLongitude() == null && waterworks.getLatitude() == null) {
            return;
        }
        Waterworks update = waterworksService.getById(waterworks.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        waterworksMapper.addPosition(waterworks.getId(), geom);
    }

    @Override
    public List<Waterworks> exportAll() {
        List<Waterworks> waterworks = waterworksMapper.selectList(null);
        return waterworks;
    }
}
