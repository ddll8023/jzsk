package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.GroundSourceWater;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
public interface GroundSourceWaterMapper extends BaseMapper<GroundSourceWater> {

    void addPosition(Long id, String geom);
    void add(String name, Double area, BigDecimal longitude, BigDecimal latitude, String waterQualityGoals, String status, String supplyObject, Double averageMineableVolume, Double annualPermittedWithdrawal, String management, String whetherEmergencySource, Date createTime, Date updateTime, String geom);
}
