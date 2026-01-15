package com.szy.mapper;

import com.szy.entity.SurfaceWaterSources;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 地表水源水 Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
public interface SurfaceWaterSourcesMapper extends BaseMapper<SurfaceWaterSources> {

    void add(String name, BigDecimal longitude, BigDecimal latitude, String type, Double area, String waterQualityObjectives, String waterSupplyContinuity, String object, String manageUnit, String waterSupplyProject, String whetherEmergencySource, Date createTime, Date updateTime, String geom);

    void addPosition(Long id, String geom);
}
