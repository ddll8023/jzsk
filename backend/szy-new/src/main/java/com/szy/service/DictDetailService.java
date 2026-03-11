package com.szy.service;

import com.szy.pojo.dto.DictDetailDTO;
import com.szy.pojo.vo.DictDetailVO;

import java.util.List;

/**
 * 字典详情服务接口
 */
public interface DictDetailService {

    /**
     * 根据ID查询字典详情
     */
    DictDetailVO getById(Long id);

    /**
     * 新增字典详情
     */
    DictDetailVO save(DictDetailDTO dto);

    /**
     * 更新字典详情
     */
    DictDetailVO update(DictDetailDTO dto);

    /**
     * 删除字典详情
     */
    void deleteById(Long id);

    /**
     * 根据字典ID查询详情列表
     */
    List<DictDetailVO> findByDictId(Long dictId);
}
