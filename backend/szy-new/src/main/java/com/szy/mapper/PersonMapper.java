package com.szy.mapper;

import com.szy.pojo.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员Mapper
 */
@Mapper
public interface PersonMapper {

    /**
     * 分页查询人员
     */
    List<Person> selectList(@Param("name") String name);

    /**
     * 根据ID查询
     */
    Person selectById(@Param("id") Long id);

    /**
     * 新增人员
     */
    int insert(Person person);

    /**
     * 更新人员
     */
    int update(Person person);

    /**
     * 删除人员
     */
    int deleteById(@Param("id") Long id);
}
