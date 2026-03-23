package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.person.PersonCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.person.PersonVO;

/**
 * 人员信息服务接口
 * 职责：定义人员信息业务方法
 */
public interface PersonService {

    /**
     * 分页查询人员列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResultVO<PersonVO> page(PersonPageQueryDTO queryDTO);

    /**
     * 根据ID查询人员详情
     * @param id 人员ID
     * @return 人员详情
     */
    PersonVO getById(Long id);

    /**
     * 创建人员
     * @param request 创建请求
     * @return 创建后的人员详情
     */
    PersonVO create(PersonCreateDTO request);

    /**
     * 更新人员
     * @param request 更新请求
     * @return 更新后的人员详情
     */
    PersonVO update(PersonUpdateDTO request);

    /**
     * 删除人员（物理删除）
     * @param id 人员ID
     */
    void delete(Long id);
}
