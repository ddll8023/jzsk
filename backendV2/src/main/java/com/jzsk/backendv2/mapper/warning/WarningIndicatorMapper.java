package com.jzsk.backendv2.mapper.warning;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningIndicatorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预警指标Mapper
 * 数据源：yjxx
 */
@Mapper
@DS("yjxx")
public interface WarningIndicatorMapper {

    /**
     * 分页查询预警指标
     *
     * @param queryDTO 查询条件
     * @param offset   偏移量
     * @param size     每页大小
     * @return 预警指标列表
     */
    List<WarningIndicatorEntity> selectPage(@Param("query") WarningIndicatorPageQueryDTO queryDTO,
                                           @Param("offset") long offset,
                                           @Param("size") long size);

    /**
     * 统计分页总数
     *
     * @param queryDTO 查询条件
     * @return 总记录数
     */
    long countPage(@Param("query") WarningIndicatorPageQueryDTO queryDTO);

    /**
     * 根据ID查询预警指标
     *
     * @param id 预警指标ID
     * @return 预警指标
     */
    WarningIndicatorEntity selectById(@Param("id") Long id);

    /**
     * 查询预警指标选项关系
     *
     * @return 测点与监测项关系列表
     */
    List<WarningIndicatorEntity> selectAllOptionRelations();

    /**
     * 新增预警指标
     *
     * @param entity 预警指标实体
     * @return 影响行数
     */
    int insert(WarningIndicatorEntity entity);

    /**
     * 更新预警指标
     *
     * @param entity 预警指标实体
     * @return 影响行数
     */
    int update(WarningIndicatorEntity entity);

    /**
     * 删除预警指标（物理删除）
     *
     * @param id 预警指标ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 统计相同测点与监测项组合数量
     *
     * @param position  测点名称
     * @param type      监测项
     * @param excludeId 排除的当前记录ID
     * @return 记录数
     */
    long countByPositionAndType(@Param("position") String position,
                                @Param("type") String type,
                                @Param("excludeId") Long excludeId);

    /**
     * 获取所有监测类型
     *
     * @return 类型列表
     */
    List<String> selectAllTypes();

    /**
     * 查询所有预警指标（用于自动预警任务）
     *
     * @return 预警指标列表
     */
    List<WarningIndicatorEntity> selectAll();

    /**
     * 根据测点和监测类型查询预警指标
     *
     * @param position 测点名称
     * @param type     监测类型
     * @return 预警指标
     */
    WarningIndicatorEntity selectByPositionAndType(@Param("position") String position,
                                                  @Param("type") String type);
}
