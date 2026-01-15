package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.Impoundment;
import com.szy.entity.IndividualFlowSites;
import com.szy.mapper.ImpoundmentMapper;
import com.szy.mapper.IndividualFlowSitesMapper;
import com.szy.service.ImpoundmentService;
import com.szy.service.IndividualFlowSitesService;
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
 * @since 2022-02-22
 */
@Service
@DS("gcdd")
public class IndividualFlowSitesServiceImpl extends ServiceImpl<IndividualFlowSitesMapper, IndividualFlowSites> implements IndividualFlowSitesService {
    @Resource
    IndividualFlowSitesMapper individualFlowSitesMapper;
    @Resource
    IndividualFlowSitesService individualFlowSitesService;
    @Override
    @Transactional
    public void addIndividualFlowSites (IndividualFlowSites individualFlowSites) {
        Date dateNow = new Date();
        String geom = "Point(" + individualFlowSites.getLongitude() + " " + individualFlowSites.getLatitude() + ")";
        individualFlowSitesMapper.add(individualFlowSites.getName(), individualFlowSites.getStationNumber(), individualFlowSites.getRtuCode(), individualFlowSites.getMeasuringStationsElements(), individualFlowSites.getSplitSiteCode(), individualFlowSites.getAddress(), individualFlowSites.getLongitude(), individualFlowSites.getLatitude(), individualFlowSites.getUpUpLimit(), individualFlowSites.getUpLimit(), individualFlowSites.getLowLimit(), individualFlowSites.getLowerLimit(), individualFlowSites.getNote(), dateNow, dateNow, geom);
    }

    @Override
    @Transactional
    public void updateIndividualFlowSites(IndividualFlowSites individualFlowSites) {
        individualFlowSitesService.updateById(individualFlowSites);
        if(individualFlowSites.getLongitude() == null && individualFlowSites.getLatitude() == null) {
            return;
        }
        IndividualFlowSites update = individualFlowSitesService.getById(individualFlowSites.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        Long id = individualFlowSites.getId();
        individualFlowSitesMapper.addPosition(id, geom);
    }

    @Override
    public List<IndividualFlowSites> exportAll() {
        List<IndividualFlowSites> individualFlowSites = individualFlowSitesMapper.selectList(null);
        return individualFlowSites;
    }
}
