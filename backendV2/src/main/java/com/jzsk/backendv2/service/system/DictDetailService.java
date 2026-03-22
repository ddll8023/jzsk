package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailUpdateDTO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;

/**
 * 字典详情服务接口
 * 职责：提供字典详情的CRUD业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
public interface DictDetailService {

    /**
     * 根据ID查询字典详情
     * @param id 字典详情ID
     * @return 字典详情VO
     */
    DictDetailVO getById(Long id);

    /**
     * 创建字典详情
     * @param request 创建请求
     * @return 字典详情VO
     */
    DictDetailVO create(DictDetailCreateDTO request);

    /**
     * 更新字典详情
     * @param request 更新请求
     * @return 字典详情VO
     */
    DictDetailVO update(DictDetailUpdateDTO request);

    /**
     * 删除字典详情
     * @param id 字典详情ID
     */
    void delete(Long id);
}
