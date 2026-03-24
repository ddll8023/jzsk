package com.jzsk.backendv2.mapper.engineering;

import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.engineering.MeasuringStationEntity;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 监测站点Mapper接口
 * 职责：监测站点表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface MeasuringStationMapper {

    /**
     * 分页查询监测站点
     * @param query 查询参数
     * @return 监测站点列表
     */
    List<MeasuringStationEntity> selectPage(@Param("query") MeasuringStationPageQueryDTO query,
                                            @Param("offset") long offset,
                                            @Param("size") long size);

    /**
     * 统计分页总数
     * @param query 查询参数
     * @return 总记录数
     */
    long countPage(@Param("query") MeasuringStationPageQueryDTO query);

    /**
     * 根据ID查询监测站点
     * @param id 监测站点ID
     * @return 监测站点实体
     */
    MeasuringStationEntity selectById(@Param("id") Long id);

    /**
     * 根据站码统计数量（用于校验站码唯一性）
     * @param code 站码
     * @param excludeId 排除的ID（更新时排除自身）
     * @return 数量
     */
    int countByCode(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * 新增监测站点
     * @param entity 监测站点实体
     * @return 影响行数
     */
    int insert(MeasuringStationEntity entity);

    /**
     * 更新监测站点
     * @param entity 监测站点实体
     * @return 影响行数
     */
    int update(MeasuringStationEntity entity);

    /**
     * 根据ID删除监测站点
     * @param id 监测站点ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询所有站点名称（用于下拉选择）
     * @return 站点实体列表
     */
    List<MeasuringStationEntity> selectNames();
}
