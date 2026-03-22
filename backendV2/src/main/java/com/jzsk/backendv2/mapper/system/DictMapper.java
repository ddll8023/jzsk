package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.dict.DictPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.DictEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典Mapper接口
 * 职责：字典表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
@Mapper
public interface DictMapper {

    /**
     * 分页查询字典
     * @param queryDTO 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 字典实体列表
     */
    List<DictEntity> selectPage(@Param("query") DictPageQueryDTO queryDTO,
                                @Param("offset") long offset,
                                @Param("size") long size);

    /**
     * 统计分页总数
     * @param queryDTO 查询条件
     * @return 总记录数
     */
    long countPage(@Param("query") DictPageQueryDTO queryDTO);

    /**
     * 根据ID查询字典（基础信息）
     * @param id 字典ID
     * @return 字典实体
     */
    DictEntity selectBaseById(@Param("id") Long id);

    /**
     * 根据ID查询字典（含详情）
     * @param id 字典ID
     * @return 字典实体
     */
    DictEntity selectById(@Param("id") Long id);

    /**
     * 根据名称统计字典数量
     * @param name 字典名称
     * @param excludeId 排除的字典ID
     * @return 数量
     */
    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * 插入字典
     * @param entity 字典实体
     * @return 影响行数
     */
    int insert(DictEntity entity);

    /**
     * 更新字典
     * @param entity 字典实体
     * @return 影响行数
     */
    int update(DictEntity entity);

    /**
     * 根据ID删除字典
     * @param id 字典ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据字典名称查询选项列表
     * @param name 字典名称
     * @return 选项列表
     */
    List<OptionVO> selectOptionsByName(@Param("name") String name);

    /**
     * 批量查询字典的详情数量
     * @param dictIds 字典ID列表
     * @return 字典ID与详情数量的映射列表
     */
    List<Map<String, Object>> selectDetailCounts(@Param("dictIds") List<Long> dictIds);
}
