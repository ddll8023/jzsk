package com.jzsk.backendv2.mapper.engineering;

import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.engineering.InspectionRecordsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检记录Mapper接口
 * 职责：巡检记录表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface InspectionRecordsMapper {

    /**
     * 分页查询巡检记录
     * @param query 查询参数
     * @param offset 偏移量
     * @param size 每页大小
     * @return 巡检记录列表
     */
    List<InspectionRecordsEntity> selectPage(@Param("query") InspectionRecordsPageQueryDTO query,
                                            @Param("offset") long offset,
                                            @Param("size") long size);

    /**
     * 统计分页总数
     * @param query 查询参数
     * @return 总记录数
     */
    long countPage(@Param("query") InspectionRecordsPageQueryDTO query);

    /**
     * 根据ID查询巡检记录
     * @param id 巡检记录ID
     * @return 巡检记录实体
     */
    InspectionRecordsEntity selectById(@Param("id") Long id);

    /**
     * 新增巡检记录
     * @param entity 巡检记录实体
     * @return 影响行数
     */
    int insert(InspectionRecordsEntity entity);

    /**
     * 更新巡检记录
     * @param entity 巡检记录实体
     * @return 影响行数
     */
    int update(InspectionRecordsEntity entity);

    /**
     * 根据ID删除巡检记录
     * @param id 巡检记录ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询全部巡检记录（用于导出）
     * @return 巡检记录列表
     */
    List<InspectionRecordsEntity> selectAllForExport();
}
