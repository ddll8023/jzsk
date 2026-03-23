package com.jzsk.backendv2.mapper.warning;

import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningFacilityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预警设施Mapper接口
 * 职责：预警设施表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface WarningFacilityMapper {

    /**
     * 根据ID查询预警设施
     * @param id 预警设施ID
     * @return 预警设施实体，无对应记录时返回null
     */
    WarningFacilityEntity selectById(@Param("id") Long id);

    /**
     * 分页查询预警设施列表
     * @param queryDTO 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 预警设施列表
     */
    List<WarningFacilityEntity> selectPage(@Param("query") WarningFacilityPageQueryDTO queryDTO,
                                          @Param("offset") long offset,
                                          @Param("size") long size);

    /**
     * 统计预警设施总数（用于分页）
     * @param queryDTO 查询条件
     * @return 总记录数
     */
    long selectCount(WarningFacilityPageQueryDTO queryDTO);

    /**
     * 插入预警设施
     * @param entity 预警设施实体
     * @return 影响行数
     */
    int insert(WarningFacilityEntity entity);

    /**
     * 更新预警设施
     * @param entity 预警设施实体
     * @return 影响行数
     */
    int update(WarningFacilityEntity entity);

    /**
     * 根据ID删除预警设施（物理删除）
     * @param id 预警设施ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
