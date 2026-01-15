package com.szy.mapper;

import com.szy.entity.IndividualFlowSites;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-22
 */
public interface IndividualFlowSitesMapper extends BaseMapper<IndividualFlowSites> {
    void addPosition(Long id, String geom);

    void add(String name, String stationNumber, String rtuCode, String measuringStationsElements, String splitSiteCode, String address, BigDecimal longitude, BigDecimal latitude, Double upUpLimit, Double upLimit, Double lowLimit, Double lowerLimit, String note, Date createTime, Date updateTime, String geom);
}
