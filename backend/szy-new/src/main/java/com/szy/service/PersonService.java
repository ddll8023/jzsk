package com.szy.service;

import com.github.pagehelper.PageInfo;
import com.szy.pojo.dto.PersonDTO;
import com.szy.pojo.dto.PersonQueryDTO;
import com.szy.pojo.vo.PersonVO;

/**
 * 人员服务接口
 */
public interface PersonService {

    /**
     * 分页查询人员
     */
    PageInfo<PersonVO> list(PersonQueryDTO queryDTO);

    /**
     * 获取人员详情
     */
    PersonVO getById(Long id);

    /**
     * 新增人员
     */
    PersonVO save(PersonDTO dto);

    /**
     * 更新人员
     */
    PersonVO update(PersonDTO dto);

    /**
     * 删除人员
     */
    void deleteById(Long id);
}
