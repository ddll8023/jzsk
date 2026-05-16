package com.jzsk.backendv2.mapper.monitor;

import com.jzsk.backendv2.pojo.entity.monitor.DeviceFaultEventLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备故障事件明细Mapper接口
 * 职责：设备故障事件明细表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface DeviceFaultEventLogMapper {

    /**
     * 新增事件明细
     * @param entity 事件明细实体
     */
    int insert(DeviceFaultEventLogEntity entity);

    /**
     * 根据故障主记录ID查询事件列表，按事件时间升序
     * @param faultRecordId 故障主记录ID
     * @return 事件明细列表
     */
    List<DeviceFaultEventLogEntity> selectByFaultRecordId(@Param("faultRecordId") Long faultRecordId);

    /**
     * 根据故障主记录ID删除事件明细
     * @param faultRecordId 故障主记录ID
     */
    int deleteByFaultRecordId(@Param("faultRecordId") Long faultRecordId);

    /**
     * 删除截止时间之前的事件明细
     * @param cutoffTime 截止时间
     */
    int deleteBeforeTime(@Param("cutoffTime") LocalDateTime cutoffTime);
}
