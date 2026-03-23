package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.person.PersonPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.PersonEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员信息Mapper接口
 * 职责：人员信息表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface PersonMapper {

    /**
     * 分页查询人员列表
     * @param queryDTO 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 人员列表
     */
    List<PersonEntity> selectPage(@Param("query") PersonPageQueryDTO queryDTO,
                                  @Param("offset") long offset,
                                  @Param("size") long size);

    /**
     * 统计分页总数
     * @param queryDTO 查询条件
     * @return 总数
     */
    long countPage(@Param("query") PersonPageQueryDTO queryDTO);

    /**
     * 根据ID查询人员
     * @param id 人员ID
     * @return 人员实体
     */
    PersonEntity selectById(@Param("id") Long id);

    /**
     * 新增人员
     * @param entity 人员实体
     * @return 影响行数
     */
    int insert(PersonEntity entity);

    /**
     * 更新人员
     * @param entity 人员实体
     * @return 影响行数
     */
    int update(PersonEntity entity);

    /**
     * 删除人员（物理删除）
     * @param id 人员ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
