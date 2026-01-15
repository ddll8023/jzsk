package com.szy.mapper;

import com.szy.entity.DataNew;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * data_new 表 Mapper 接口
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Mapper
@DS("pgsql")
public interface DataNewMapper extends BaseMapper<DataNew> {
    /**
     * 查询每个point_id最新一条数据
     */
    @Select("SELECT DISTINCT ON (point_id) * FROM data_new ORDER BY point_id, time DESC")
    java.util.List<DataNew> selectLatestForAllPoints();
} 