package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.Town;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TownMapper extends BaseMapper<Town>{

    List<String> getAllNames();
}
