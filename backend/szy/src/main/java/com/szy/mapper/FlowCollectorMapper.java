package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.FlowCollector;
import com.szy.entity.WaterLevelCollector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@DS("collector")
public interface FlowCollectorMapper extends BaseMapper<FlowCollector> {
    @Select("SELECT *\n" +
            "FROM wr_mp_q_r\n" +
            "WHERE (MP_CD, TS) IN (\n" +
            "    SELECT MP_CD, MAX(TS)\n" +
            "    FROM wr_mp_q_r\n" +
            "    GROUP BY MP_CD\n" +
            ");")
    FlowCollector selectLastRecord();
}
