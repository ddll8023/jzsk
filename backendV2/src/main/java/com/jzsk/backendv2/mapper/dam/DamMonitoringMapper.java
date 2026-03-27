package com.jzsk.backendv2.mapper.dam;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.dam.DataNewEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大坝监测Mapper
 * 数据源：pgsql（PostgreSQL）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface DamMonitoringMapper {

	// ==================== data_new MCU传感器数据（pgsql数据源） ====================

	/**
	 * 查询每个测点最新数据
	 *
	 * @return 各测点最新数据列表
	 */
	@DS("pgsql")
	List<DataNewEntity> selectLatestForAllPoints();

	/**
	 * 根据测点编号查询最新一条数据
	 *
	 * @param pointId 测点编号
	 * @return 最新数据
	 */
	@DS("pgsql")
	DataNewEntity selectLatestByPointId(@Param("pointId") String pointId);
}
