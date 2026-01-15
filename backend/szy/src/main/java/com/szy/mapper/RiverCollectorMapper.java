package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.RiverCollector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@DS("collector")
public interface RiverCollectorMapper extends BaseMapper<RiverCollector> {
    @Select("SELECT * FROM st_river_r WHERE (STCD, TM) IN (SELECT STCD, MAX(TM) FROM st_river_r GROUP BY STCD);")
    List<RiverCollector> selectLastRecord();
}
