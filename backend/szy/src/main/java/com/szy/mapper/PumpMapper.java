package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.Pump;
import com.szy.entity.PumpStation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PumpMapper extends BaseMapper<Pump> {
}
