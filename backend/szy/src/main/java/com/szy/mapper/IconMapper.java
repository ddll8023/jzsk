package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.Impoundment;
import com.szy.entity.icon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import javax.swing.*;

@Mapper
public interface IconMapper extends BaseMapper<icon>  {

    @Update("UPDATE icons SET img = #{fileName} WHERE name = '工程简介'")
    int updateImgByName(String fileName);
}
