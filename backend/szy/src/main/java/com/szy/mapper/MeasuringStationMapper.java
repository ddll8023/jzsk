package com.szy.mapper;

import com.szy.entity.MeasuringStation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@Mapper
public interface MeasuringStationMapper extends BaseMapper<MeasuringStation> {
    void add(String code, String name, String waterName, String riverName, String monitorCode, String addressCode, Date establishDate, BigDecimal longitude, BigDecimal latitude, String note, Date createTime, Date updateTime, String geom);
    void addPosition(Long id, String geom);
    @Select("select name from measuring_station order by create_time desc")
    List<String> getAllNames();
    @Select("select code from measuring_station order by create_time desc")
    List<String> getAllCodes();
    @Select("select * from measuring_station where code = #{mpCd}")
    MeasuringStation getByCode(String mpCd);
}
