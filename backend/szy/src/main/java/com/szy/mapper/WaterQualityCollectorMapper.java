package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.WaterLevelCollector;
import com.szy.entity.WaterQualityCollector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@DS("eb_database")
public interface WaterQualityCollectorMapper extends BaseMapper<WaterQualityCollector> {

    @Select("SELECT * FROM bb WHERE dateTime = (SELECT MAX(dateTime) FROM bb);")
    WaterQualityCollector selectLastRecord();
}
