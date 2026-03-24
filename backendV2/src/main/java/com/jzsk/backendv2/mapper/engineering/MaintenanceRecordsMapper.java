package com.jzsk.backendv2.mapper.engineering;

import com.jzsk.backendv2.pojo.entity.engineering.MaintenanceRecordsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 养护记录Mapper接口
 * 职责：养护记录表的数据库操作
 * 数据源：gcdd（通过 @DS 注解在 Service 层切换，此处不再标注）
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface MaintenanceRecordsMapper {

    /**
     * 分页查询养护记录
     * @param query 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 养护记录列表
     */
    List<MaintenanceRecordsEntity> selectPage(
            @Param("query") Object query,
            @Param("offset") long offset,
            @Param("size") long size);

    /**
     * 统计分页总数
     * @param query 查询条件
     * @return 总记录数
     */
    long countPage(@Param("query") Object query);

    /**
     * 根据ID查询养护记录
     * @param id 养护记录ID
     * @return 养护记录实体，无对应记录时返回null
     */
    MaintenanceRecordsEntity selectById(@Param("id") Long id);

    /**
     * 新增养护记录
     * @param entity 养护记录实体
     * @return 影响行数
     */
    int insert(MaintenanceRecordsEntity entity);

    /**
     * 更新养护记录
     * @param entity 养护记录实体（必须包含id）
     * @return 影响行数
     */
    int update(MaintenanceRecordsEntity entity);

    /**
     * 根据ID删除养护记录（物理删除）
     * @param id 养护记录ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询全部养护记录用于导出
     * @return 全部养护记录列表
     */
    List<MaintenanceRecordsEntity> selectAllForExport();
}
