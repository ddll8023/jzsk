package com.jzsk.backendv2.mapper.warning;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.dto.warning.WarningPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预警信息Mapper
 * 数据源：yjxx
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
@DS("yjxx")
public interface WarningMapper {

    /**
     * 分页查询预警信息
     *
     * @param queryDTO 查询条件
     * @param offset   偏移量
     * @param size     每页大小
     * @return 预警信息列表
     */
    List<WarningInformationEntity> selectPage(@Param("query") WarningPageQueryDTO queryDTO,
                                              @Param("offset") long offset,
                                              @Param("size") long size);

    /**
     * 统计分页总数
     *
     * @param queryDTO 查询条件
     * @return 总记录数
     */
    long countPage(@Param("query") WarningPageQueryDTO queryDTO);

    /**
     * 根据ID查询预警信息
     *
     * @param id 预警ID
     * @return 预警信息
     */
    WarningInformationEntity selectById(@Param("id") Long id);

    /**
     * 新增预警信息
     *
     * @param entity 预警信息实体
     * @return 影响行数
     */
    int insert(WarningInformationEntity entity);

    /**
     * 更新预警信息
     *
     * @param entity 预警信息实体
     * @return 影响行数
     */
    int update(WarningInformationEntity entity);

    /**
     * 删除预警信息（物理删除）
     *
     * @param id 预警ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询指定位置和类型、且状态为未解除的预警记录（用于自动预警去重）
     *
     * @param position 监测位置
     * @param type     监测类型
     * @param status   预警状态
     * @return 符合条件的预警记录，未找到则返回null
     */
    WarningInformationEntity selectUnresolvedByPositionAndType(@Param("position") String position,
                                                               @Param("type") String type,
                                                               @Param("status") String status);
}
