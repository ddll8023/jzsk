package com.szy.mapper;

import com.szy.pojo.entity.Organization;
import com.szy.pojo.vo.OrganizationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构Mapper
 */
public interface OrganizationMapper {

    /**
     * 分页查询
     */
    List<OrganizationVO> selectList(@Param("name") String name);

    /**
     * 根据ID查询
     */
    OrganizationVO selectById(@Param("id") Long id);

    /**
     * 新增
     */
    int insert(Organization organization);

    /**
     * 更新
     */
    int update(Organization organization);

    /**
     * 删除
     */
    int deleteById(@Param("id") Long id);
}
