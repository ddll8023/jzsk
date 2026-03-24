package com.jzsk.backendv2.mapper.report;

import com.jzsk.backendv2.pojo.dto.report.DutyLogPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.report.DutyLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 值班日志Mapper接口
 * 职责：值班日志表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface DutyLogMapper {

    /**
     * 插入值班日志
     * @param entity 值班日志实体
     * @return 影响行数
     */
    int insert(DutyLogEntity entity);

    /**
     * 更新值班日志
     * @param entity 值班日志实体
     * @return 影响行数
     */
    int update(DutyLogEntity entity);

    /**
     * 根据ID删除值班日志
     * @param id 值班日志ID
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 批量删除值班日志
     * @param ids ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据ID查询值班日志
     * @param id 值班日志ID
     * @return 值班日志实体
     */
    DutyLogEntity selectById(Integer id);

    /**
     * 分页查询值班日志
     * @param queryDTO 查询参数
     * @param offset 偏移量
     * @param size 每页大小
     * @return 值班日志列表
     */
    List<DutyLogEntity> selectPage(@Param("query") DutyLogPageQueryDTO queryDTO, @Param("offset") long offset, @Param("size") long size);

    /**
     * 统计总数
     * @param queryDTO 查询参数
     * @return 总数
     */
    long countPage(@Param("query") DutyLogPageQueryDTO queryDTO);
}
