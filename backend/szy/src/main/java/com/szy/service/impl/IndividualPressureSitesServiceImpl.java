package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.IndividualFlowSites;
import com.szy.entity.IndividualPressureSites;
import com.szy.mapper.IndividualFlowSitesMapper;
import com.szy.mapper.IndividualPressureSitesMapper;
import com.szy.service.IndividualFlowSitesService;
import com.szy.service.IndividualPressureSitesService;
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
 * @since 2022-02-21
 */
@Service
@DS("gcdd")
public class IndividualPressureSitesServiceImpl extends ServiceImpl<IndividualPressureSitesMapper, IndividualPressureSites> implements IndividualPressureSitesService {
    @Resource
    IndividualPressureSitesMapper individualPressureSitesMapper;
    @Resource
    IndividualPressureSitesService individualPressureSitesService;
    @Override
    @Transactional
    public void addIndividualPressureSites (IndividualPressureSites individualPressureSites) {
        Date dateNow = new Date();
        String geom = "Point(" + individualPressureSites.getLongitude() + " " + individualPressureSites.getLatitude() + ")";
        individualPressureSitesMapper.add(individualPressureSites.getName(), individualPressureSites.getStationNumber(), individualPressureSites.getRtuCode(), individualPressureSites.getMeasuringStationsElements(), individualPressureSites.getSplitSiteCode(), individualPressureSites.getAddress(), individualPressureSites.getLongitude(), individualPressureSites.getLatitude(), individualPressureSites.getUpUpLimit(), individualPressureSites.getUpLimit(), individualPressureSites.getLowLimit(), individualPressureSites.getLowerLimit(), individualPressureSites.getNote(), dateNow, dateNow, geom);
    }

    @Override
    @Transactional
    public void updateIndividualPressureSites(IndividualPressureSites individualPressureSites) {
        individualPressureSitesService.updateById(individualPressureSites);
        if(individualPressureSites.getLongitude() == null && individualPressureSites.getLatitude() == null) {
            return;
        }
        IndividualPressureSites update = individualPressureSitesService.getById(individualPressureSites.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        Long id = individualPressureSites.getId();
        individualPressureSitesMapper.addPosition(id, geom);
    }

    @Override
    public List<IndividualPressureSites> exportAll() {
        List<IndividualPressureSites> individualPressureSites = individualPressureSitesMapper.selectList(null);
        return individualPressureSites;
    }
}
