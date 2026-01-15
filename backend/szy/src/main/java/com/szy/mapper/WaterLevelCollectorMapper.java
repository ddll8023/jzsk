package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.WaterLevel;
import com.szy.entity.WaterLevelCollector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@DS("collector")
public interface WaterLevelCollectorMapper extends BaseMapper<WaterLevelCollector> {

    @Select("SELECT * FROM wr_mp_z_r WHERE TS = (SELECT MAX(TS) FROM wr_mp_z_r);")
    WaterLevelCollector selectLastRecord();
}
