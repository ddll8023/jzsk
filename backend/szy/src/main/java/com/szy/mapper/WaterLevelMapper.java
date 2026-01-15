package com.szy.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.WaterLevel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 水位监测
 * @author admin
 * @date 2024/06/16 17:19
 */
@Mapper
@DS("gcdd")
public interface WaterLevelMapper extends BaseMapper<WaterLevel> {
    /**
     * 获取水位监测值
     * @param map
     * @return java.util.List<java.lang.Double>
     * @author admin
     * @date 2024/06/17 15:49
     */
    List<Double> getValuesByMap(Map map);

    /**
     * 获取时间
     * @param map
     * @return java.util.List<java.lang.Double>
     * @author admin
     * @date 2024/06/17 18:33
     */
    List<DateTime> getTimesByMap(Map map);

    /**
     * 根据站点编码和监测时间查询水位记录
     * @param code
     * @param monitorTime
     * @return com.szy.entity.WaterLevel
     */
    // WaterLevel findByCodeAndMonitorTime(String code, LocalDateTime monitorTime);
}
