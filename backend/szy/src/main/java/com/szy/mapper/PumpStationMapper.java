package com.szy.mapper;

import com.szy.entity.PumpStation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-20
 */
@Mapper
public interface PumpStationMapper extends BaseMapper<PumpStation> {
    void addPosition(Long id, String geom);

    void add(String waterSupplyProject, String code, String name, String type, String company, BigDecimal longitude, BigDecimal latitude, String address, String operationMode, Integer number, Double designScale, Double installedCapacity, Double lift, Date date, Date createTime, Date updateTime, String geom);
}
