package com.jzsk.backendv2.mapper.engineering;

import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.engineering.MeasuringItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测项Mapper接口
 * 职责：测项表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface MeasuringItemMapper {

    /**
     * 分页查询测项
     * @param query 查询参数
     * @param offset 偏移量
     * @param size 每页大小
     * @return 测项列表
     */
    List<MeasuringItemEntity> selectPage(@Param("query") MeasuringItemPageQueryDTO query,
                                         @Param("offset") long offset,
                                         @Param("size") long size);

    /**
     * 统计分页总数
     * @param query 查询参数
     * @return 总记录数
     */
    long countPage(@Param("query") MeasuringItemPageQueryDTO query);

    /**
     * 根据ID查询测项
     * @param id 测项ID
     * @return 测项实体
     */
    MeasuringItemEntity selectById(@Param("id") Long id);

    /**
     * 根据测项编号统计数量（用于校验编号唯一性）
     * @param number 测项编号
     * @param excludeId 排除的ID（更新时排除自身）
     * @return 数量
     */
    int countByNumber(@Param("number") String number, @Param("excludeId") Long excludeId);

    /**
     * 新增测项
     * @param entity 测项实体
     * @return 影响行数
     */
    int insert(MeasuringItemEntity entity);

    /**
     * 更新测项
     * @param entity 测项实体
     * @return 影响行数
     */
    int update(MeasuringItemEntity entity);

    /**
     * 根据ID删除测项
     * @param id 测项ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询所有测项名称（用于下拉选择）
     * @return 测项实体列表
     */
    List<MeasuringItemEntity> selectNames();

    /**
     * 查询所有测项（用于导出）
     * @return 测项实体列表
     */
    List<MeasuringItemEntity> selectAll();
}
