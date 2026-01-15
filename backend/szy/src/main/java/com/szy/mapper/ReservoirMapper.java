package com.szy.mapper;

import com.szy.entity.Reservoir;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-23
 */
public interface ReservoirMapper extends BaseMapper<Reservoir> {
    void addPosition(Long id, String geom);

    void add(String waterSupply, String code, String name, BigDecimal longitude, BigDecimal latitude, String locate, String registrationNumber, String administrationDivision, String level, String scale, Double totalStorageCapacity, Double regulatingStorageCapacity, Double deadStorage, Double designFloodLevel, Double normalStorageLevel, Double deadWaterLevel, Date date, String manageUnit, String waterSupplyArea, Date createTime, Date updateTime, String geom);

    /**
     * 获取所有的水库名称
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/06/18 20:07
     */
    List<String> getAllReservoirs();
}
