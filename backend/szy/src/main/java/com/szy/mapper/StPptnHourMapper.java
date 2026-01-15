package com.szy.mapper;

import com.szy.entity.StPptnHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.dynamic.datasource.annotation.DS;

/**
 * <p>
 * 逐小时降雨量数据表 Mapper 接口
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@DS("dbo")
@Mapper
public interface StPptnHourMapper extends BaseMapper<StPptnHour> {
    @Select("SELECT TOP 1 * FROM ST_PPTN_HOUR WHERE STCD = #{stcd} ORDER BY TM DESC")
    StPptnHour selectLatestByStcd(@Param("stcd") String stcd);
}
