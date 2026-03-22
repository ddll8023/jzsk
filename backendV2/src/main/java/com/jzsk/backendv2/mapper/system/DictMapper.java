package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.dict.DictPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.DictEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictMapper {

    List<DictEntity> selectPage(@Param("query") DictPageQueryDTO queryDTO,
                                @Param("offset") long offset,
                                @Param("size") long size);

    long countPage(@Param("query") DictPageQueryDTO queryDTO);

    DictEntity selectBaseById(@Param("id") Long id);

    DictEntity selectById(@Param("id") Long id);

    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    int insert(DictEntity entity);

    int update(DictEntity entity);

    int deleteById(@Param("id") Long id);

    List<OptionVO> selectOptionsByName(@Param("name") String name);

    List<Map<String, Object>> selectDetailCounts(@Param("dictIds") List<Long> dictIds);
}
