package com.szy.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.Flow;
import com.szy.entity.WaterLevel;
import org.apache.ibatis.annotations.Mapper;

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
public interface FlowMapper extends BaseMapper<Flow> {
    /**
     * 获取流量监测值
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
     * 根据站点编码和时间查询流量记录
     * @param mpCd
     * @param tm
     * @return com.szy.entity.Flow
     */
    // @Select("SELECT * FROM flow WHERE mp_cd = #{mpCd} AND tm = #{tm}")
    // Flow findByMpCdAndTm(@Param("mpCd") String mpCd, @Param("tm") LocalDateTime tm);
}
