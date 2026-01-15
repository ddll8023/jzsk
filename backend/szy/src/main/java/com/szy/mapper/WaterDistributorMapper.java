package com.szy.mapper;

import com.szy.entity.WaterDistributor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-22
 */
public interface WaterDistributorMapper extends BaseMapper<WaterDistributor> {

    void addPosition(Long id, String geom);

    void add(String name, String county, String town, String village, BigDecimal longitude, BigDecimal latitude, String waterSupply, Date date, String manageUnit, String address, String rtuCode, String note, Date createTime, Date updateTime, String geom);
}
