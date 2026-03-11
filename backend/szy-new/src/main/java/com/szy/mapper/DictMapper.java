package com.szy.mapper;

import com.szy.pojo.entity.Dict;
import com.szy.pojo.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典Mapper接口
 */
@Mapper
public interface DictMapper {

    /**
     * 分页查询字典列表
     */
    List<Dict> list(@Param("blurry") String blurry, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询总数
     */
    long count(@Param("blurry") String blurry);

    /**
     * 根据ID查询字典
     */
    Dict findById(@Param("id") Long id);

    /**
     * 根据名称查询字典
     */
    Dict findByName(@Param("name") String name);

    /**
     * 查询所有字典名称
     */
    List<String> findAllNames();

    /**
     * 根据字典名称查询字典
     */
    Dict findByDictName(@Param("name") String name);

    /**
     * 新增字典
     */
    int insert(Dict dict);

    /**
     * 更新字典
     */
    int update(Dict dict);

    /**
     * 删除字典
     */
    int deleteById(@Param("id") Long id);
}
