package com.szy.mapper;

import com.szy.entity.VideoConfiguration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Mapper
public interface VideoConfigurationMapper extends BaseMapper<VideoConfiguration> {

    List<String> getAllTypes();

    List<String> getAllTowns();

    List<String> getVillagesByTown(String town);

    @Select("SELECT distinct(name) FROM video_configuration")
    List<String> getAllNames();

    @Select("SELECT village FROM video_configuration WHERE village IS NOT NULL ORDER BY create_time DESC")
    List<String> getAllCodes();
}
