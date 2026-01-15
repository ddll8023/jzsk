package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.DictDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictDetailMapper extends BaseMapper<DictDetail> {
    /**
     * 根据字典id获取字典详情id
     * @param id
     * @return
     */
    List<Long> selectDetailsById(Long id);
}
