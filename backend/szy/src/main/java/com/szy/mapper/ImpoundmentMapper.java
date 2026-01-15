package com.szy.mapper;

import com.szy.entity.Impoundment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
public interface ImpoundmentMapper extends BaseMapper<Impoundment> {
    void addPosition(Long id, String geom);

    void add(String company, String name, String address, BigDecimal longitude, BigDecimal latitude, Date date, String manageUnit, Double volume, Double controlWaterLevel, String responsiblePerson, String phone, String rtuCode, String note, Date createTime, Date updateTime, String geom);
}
