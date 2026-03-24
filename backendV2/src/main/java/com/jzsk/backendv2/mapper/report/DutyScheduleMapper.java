package com.jzsk.backendv2.mapper.report;

import com.jzsk.backendv2.pojo.dto.report.DutySchedulePageQueryDTO;
import com.jzsk.backendv2.pojo.entity.report.DutyScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 值班安排Mapper接口
 * 职责：值班安排表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface DutyScheduleMapper {

    /**
     * 插入值班安排
     * @param entity 值班安排实体
     * @return 影响行数
     */
    int insert(DutyScheduleEntity entity);

    /**
     * 更新值班安排
     * @param entity 值班安排实体
     * @return 影响行数
     */
    int update(DutyScheduleEntity entity);

    /**
     * 根据ID删除值班安排
     * @param id 值班安排ID
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 批量删除值班安排
     * @param ids ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据ID查询值班安排
     * @param id 值班安排ID
     * @return 值班安排实体
     */
    DutyScheduleEntity selectById(Integer id);

    /**
     * 分页查询值班安排
     * @param queryDTO 查询参数
     * @param offset 偏移量
     * @param size 每页大小
     * @return 值班安排列表
     */
    List<DutyScheduleEntity> selectPage(@Param("query") DutySchedulePageQueryDTO queryDTO, @Param("offset") long offset, @Param("size") long size);

    /**
     * 统计总数
     * @param queryDTO 查询参数
     * @return 总数
     */
    long countPage(@Param("query") DutySchedulePageQueryDTO queryDTO);
}
