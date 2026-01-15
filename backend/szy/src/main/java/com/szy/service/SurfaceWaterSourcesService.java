package com.szy.service;

import com.szy.entity.MeasuringStation;
import com.szy.entity.SurfaceWaterSources;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地表水源水 服务类
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
public interface SurfaceWaterSourcesService extends IService<SurfaceWaterSources> {
    void saveSurfaceWaterSources(SurfaceWaterSources surfaceWaterSources);
    void updateSurfaceWaterSources(SurfaceWaterSources surfaceWaterSources);
    List<SurfaceWaterSources> exportAll();
}
