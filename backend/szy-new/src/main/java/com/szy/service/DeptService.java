package com.szy.service;

import com.github.pagehelper.PageInfo;
import com.szy.pojo.dto.DeptDTO;
import com.szy.pojo.dto.DeptQueryDTO;
import com.szy.pojo.vo.DeptVO;

/**
 * 部门服务接口
 */
public interface DeptService {

    /**
     * 分页查询部门
     */
    PageInfo<DeptVO> list(DeptQueryDTO queryDTO);

    /**
     * 获取部门详情
     */
    DeptVO getById(Long id);

    /**
     * 新增部门
     */
    DeptVO save(DeptDTO dto);

    /**
     * 更新部门
     */
    DeptVO update(DeptDTO dto);

    /**
     * 删除部门
     */
    void deleteById(Long id);
}
