package com.jzsk.backendv2.mapper.monitor;

import com.jzsk.backendv2.pojo.vo.monitor.GateDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 闸门数据Mapper接口
 * 职责：闸门数据的数据库操作
 * 数据源：zkxt（SQL Server）
 */
@Mapper
public interface GateMapper {

    /**
     * 东干渠数据查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 闸门数据列表
     */
    List<GateDataVO> selectDgq(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 电站蝶阀数据查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 闸门数据列表
     */
    List<GateDataVO> selectDzdf(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 取水塔数据查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 闸门数据列表
     */
    List<GateDataVO> selectQst(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 西干渠数据查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 闸门数据列表
     */
    List<GateDataVO> selectXgq(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 溢洪道数据查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 闸门数据列表
     */
    List<GateDataVO> selectYhd(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
