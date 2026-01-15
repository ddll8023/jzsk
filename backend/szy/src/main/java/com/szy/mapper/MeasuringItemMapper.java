package com.szy.mapper;

import com.szy.entity.MeasuringItem;
import com.szy.entity.MeasuringStation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
public interface MeasuringItemMapper extends BaseMapper<MeasuringItem> {

    void add(String number, String name, String unit, Date createTime, Date updateTime);
}
