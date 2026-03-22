package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.entity.system.DictDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictDetailMapper {

    DictDetailEntity selectById(@Param("id") Long id);

    List<DictDetailEntity> selectByDictId(@Param("dictId") Long dictId);

    int countByDictId(@Param("dictId") Long dictId);

    int countByDictIdAndLabel(@Param("dictId") Long dictId,
                              @Param("label") String label,
                              @Param("excludeId") Long excludeId);

    int insert(DictDetailEntity entity);

    int update(DictDetailEntity entity);

    int deleteById(@Param("id") Long id);

    int deleteByDictId(@Param("dictId") Long dictId);
}
