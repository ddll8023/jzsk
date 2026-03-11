package com.szy.mapper;

import com.szy.pojo.entity.DictDetail;
import com.szy.pojo.vo.DictDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典详情Mapper接口
 */
@Mapper
public interface DictDetailMapper {

    /**
     * 根据字典ID查询详情列表
     */
    List<DictDetail> findByDictId(@Param("dictId") Long dictId);

    /**
     * 根据ID查询详情
     */
    DictDetail findById(@Param("id") Long id);

    /**
     * 检查详情是否存在（根据dictId和value）
     */
    int countByDictIdAndValue(@Param("dictId") Long dictId, @Param("value") String value, @Param("excludeId") Long excludeId);

    /**
     * 新增字典详情
     */
    int insert(DictDetail dictDetail);

    /**
     * 更新字典详情
     */
    int update(DictDetail dictDetail);

    /**
     * 删除字典详情
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据字典ID删除详情
     */
    int deleteByDictId(@Param("dictId") Long dictId);
}
