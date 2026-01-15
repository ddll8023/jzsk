package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.WarningInformation;
import com.szy.entity.WaterDistributor;
import com.szy.mapper.WaterDistributorMapper;
import com.szy.service.WaterDistributorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2022-01-22
 */
@Service
@DS("gcdd")
public class WaterDistributorServiceImpl extends ServiceImpl<WaterDistributorMapper, WaterDistributor> implements WaterDistributorService {
    @Resource
    WaterDistributorMapper waterDistributorMapper;
    @Resource
    WaterDistributorService waterDistributorService;

    @Override
    @Transactional
    public void addWaterDistributor(WaterDistributor waterDistributor) {
        Date dateNow = new Date();
        String geom = "Point(" + waterDistributor.getLongitude() + " " + waterDistributor.getLatitude() + ")";
        waterDistributorMapper.add(waterDistributor.getName(), waterDistributor.getCounty(), waterDistributor.getTown(), waterDistributor.getVillage(), waterDistributor.getLongitude(), waterDistributor.getLatitude(), waterDistributor.getWaterSupply(), waterDistributor.getDate(), waterDistributor.getManageUnit(), waterDistributor.getAddress(), waterDistributor.getRtuCode(),waterDistributor.getNote(), dateNow, dateNow, geom);
    }

    @Override
    @Transactional
    public void updateWaterDistributor(WaterDistributor waterDistributor) {
        waterDistributorService.updateById(waterDistributor);
        if(waterDistributor.getLongitude() == null && waterDistributor.getLatitude() == null) {
            return;
        }
        WaterDistributor update = waterDistributorService.getById(waterDistributor.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        waterDistributorMapper.addPosition(waterDistributor.getId(), geom);
    }

    @Override
    public List<WaterDistributor> exportAll() {
        List<WaterDistributor> waterDistributors = waterDistributorMapper.selectList(null);
        return waterDistributors;
    }
}
