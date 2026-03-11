package com.szy.service;

import com.github.pagehelper.PageInfo;
import com.szy.pojo.dto.DictDTO;
import com.szy.pojo.dto.DictQueryDTO;
import com.szy.pojo.vo.DictVO;

import java.util.List;

/**
 * 字典服务接口
 */
public interface DictService {

    /**
     * 分页查询字典列表
     */
    PageInfo<DictVO> list(DictQueryDTO queryDTO);

    /**
     * 根据ID查询字典
     */
    DictVO getById(Long id);

    /**
     * 新增字典
     */
    DictVO save(DictDTO dto);

    /**
     * 更新字典
     */
    DictVO update(DictDTO dto);

    /**
     * 删除字典
     */
    void deleteById(Long id);

    /**
     * 查询所有字典名称（用于kinds接口）
     */
    List<String> findAllNames();

    /**
     * 根据字典名称查询详情列表（用于LVs接口）
     */
    List<DictVO> findByDictName(String name);
}
