package com.szy.mapper;

import com.szy.entity.Waterworks;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-24
 */
public interface WaterworksMapper extends BaseMapper<Waterworks> {

    void addPosition(Long id, String geom);

    void add(String waterSupply, String code, String name, String address, String manageUnit, BigDecimal longitude, BigDecimal latitude, Double designScale, String waterSupplyRange, Double waterSupplyLoadRate, Double population, String responsiblePerson, String phone, Date date, Date createTime, Date updateTime, String geom);
}
