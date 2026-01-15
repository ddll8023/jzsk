package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.SurfaceWaterSources;
import com.szy.mapper.MeasuringStationMapper;
import com.szy.mapper.SurfaceWaterSourcesMapper;
import com.szy.service.MeasuringStationService;
import com.szy.service.SurfaceWaterSourcesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 地表水源水 服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@Service
@DS("gcdd")
public class SurfaceWaterSourcesServiceImpl extends ServiceImpl<SurfaceWaterSourcesMapper, SurfaceWaterSources> implements SurfaceWaterSourcesService {
    @Resource
    SurfaceWaterSourcesMapper surfaceWaterSourcesMapper;
    @Resource
    SurfaceWaterSourcesService surfaceWaterSourcesService;

    @Override
    @Transactional
    public void saveSurfaceWaterSources(SurfaceWaterSources surfaceWaterSources) {
        Date dateNow = new Date();
        String geom = "Point(" + surfaceWaterSources.getLongitude() + " " + surfaceWaterSources.getLatitude() + ")";
        surfaceWaterSourcesMapper.add(surfaceWaterSources.getName(), surfaceWaterSources.getLongitude(), surfaceWaterSources.getLatitude(), surfaceWaterSources.getType(), surfaceWaterSources.getArea(), surfaceWaterSources.getWaterQualityObjectives(), surfaceWaterSources.getWaterSupplyContinuity(), surfaceWaterSources.getObject(), surfaceWaterSources.getManageUnit(), surfaceWaterSources.getWaterSupplyProject(),surfaceWaterSources.getWhetherEmergencySource(), dateNow, dateNow, geom);
    }

    @Override
    @Transactional
    public void updateSurfaceWaterSources(SurfaceWaterSources surfaceWaterSources) {
        surfaceWaterSourcesService.updateById(surfaceWaterSources);
        if(surfaceWaterSources.getLongitude() ==  null && surfaceWaterSources.getLatitude() == null) {
            return;
        }
        SurfaceWaterSources update = surfaceWaterSourcesService.getById(surfaceWaterSources.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        surfaceWaterSourcesMapper.addPosition(surfaceWaterSources.getId(), geom);
    }

    @Override
    public List<SurfaceWaterSources> exportAll() {
        List<SurfaceWaterSources> surfaceWaterSources = surfaceWaterSourcesMapper.selectList(null);
        return surfaceWaterSources;
    }
}
