package com.jzsk.backendv2.mapper.monitor;

import com.jzsk.backendv2.pojo.dto.monitor.DeviceFaultPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.monitor.DeviceFaultRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备故障记录Mapper接口
 * 职责：设备故障记录表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface DeviceFaultRecordMapper {

    /**
     * 新增故障记录
     * @param entity 故障记录实体
     */
    int insert(DeviceFaultRecordEntity entity);

    /**
     * 根据活跃键查询活跃故障
     * @param activeKey 活跃故障唯一键
     * @return 活跃故障记录，无对应记录时返回null
     */
    DeviceFaultRecordEntity selectByActiveKey(@Param("activeKey") String activeKey);

    /**
     * 更新活跃故障状态
     * @param entity 故障记录实体
     */
    int updateActive(DeviceFaultRecordEntity entity);

    /**
     * 标记故障为已恢复
     * @param entity 故障记录实体
     */
    int updateResolved(DeviceFaultRecordEntity entity);

    /**
     * 分页查询故障记录
     * @param query 查询参数
     * @param offset 偏移量
     * @param size 每页大小
     * @return 故障记录列表
     */
    List<DeviceFaultRecordEntity> selectPage(@Param("query") DeviceFaultPageQueryDTO query,
                                             @Param("offset") long offset,
                                             @Param("size") long size);

    /**
     * 统计故障记录总数
     * @param query 查询参数
     * @return 总记录数
     */
    long countPage(@Param("query") DeviceFaultPageQueryDTO query);

    /**
     * 根据ID删除故障记录
     * @param id 故障记录ID
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除截止时间之前的故障记录
     * @param cutoffTime 截止时间
     */
    int deleteBeforeTime(@Param("cutoffTime") LocalDateTime cutoffTime);
}
