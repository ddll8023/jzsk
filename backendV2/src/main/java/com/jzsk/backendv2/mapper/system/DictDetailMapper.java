package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.entity.system.DictDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典详情Mapper接口
 * 职责：字典详情表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface DictDetailMapper {

    /**
     * 根据ID查询字典详情
     * @param id 字典详情ID
     * @return 字典详情实体
     */
    DictDetailEntity selectById(@Param("id") Long id);

    /**
     * 根据字典ID查询详情列表
     * @param dictId 字典ID
     * @return 字典详情列表
     */
    List<DictDetailEntity> selectByDictId(@Param("dictId") Long dictId);

    /**
     * 根据字典ID统计详情数量
     * @param dictId 字典ID
     * @return 数量
     */
    int countByDictId(@Param("dictId") Long dictId);

    /**
     * 根据字典ID和标签统计数量
     * @param dictId 字典ID
     * @param label 标签
     * @param excludeId 排除的详情ID
     * @return 数量
     */
    int countByDictIdAndLabel(@Param("dictId") Long dictId,
                              @Param("label") String label,
                              @Param("excludeId") Long excludeId);

    /**
     * 插入字典详情
     * @param entity 字典详情实体
     * @return 影响行数
     */
    int insert(DictDetailEntity entity);

    /**
     * 更新字典详情
     * @param entity 字典详情实体
     * @return 影响行数
     */
    int update(DictDetailEntity entity);

    /**
     * 根据ID删除字典详情
     * @param id 字典详情ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据字典ID删除所有详情
     * @param dictId 字典ID
     * @return 影响行数
     */
    int deleteByDictId(@Param("dictId") Long dictId);
}
