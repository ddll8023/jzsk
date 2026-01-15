package com.szy.mapper;

import com.szy.entity.InspectionRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
public interface InspectionRecordsMapper extends BaseMapper<InspectionRecords> {
    void add(
 @Param("project") String project,
@Param("longitude") BigDecimal longitude,
 @Param("latitude") BigDecimal latitude,
 @Param("type") String type,
 @Param("abnormal") String abnormal,
 @Param("situation") String situation,
 @Param("solve") String solve,
 @Param("image") String image,
 @Param("person") String person,
 @Param("date") Date date,
 @Param("createTime") Date createTime,
 @Param("updateTime") Date updateTime,
 @Param("geom") String geom
    );
    void addPosition(Long id, String geom);

    /**
     * 获取所有工程站点
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/08 19:25
     */
    List<String> getAllProjects();

}
