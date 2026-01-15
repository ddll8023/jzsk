package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.SensorPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@DS("pgsql")
public interface SensorPointMapper extends BaseMapper<SensorPoint> {
    @Select({"<script>",
            "SELECT id, name FROM sensor_point WHERE id IN ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<SensorPoint> selectNameByIds(List<Long> ids);
}
