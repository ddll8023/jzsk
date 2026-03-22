package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.dict.DictCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictOptionQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictUpdateDTO;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.TreeOptionVO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictVO;

import java.util.List;

/**
 * 字典服务接口
 * 职责：提供字典管理和字典选项查询的业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
public interface DictService {

    /**
     * 分页查询字典
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResultVO<DictVO> page(DictPageQueryDTO queryDTO);

    /**
     * 根据ID查询字典
     * @param id 字典ID
     * @return 字典VO
     */
    DictVO getById(Long id);

    /**
     * 创建字典
     * @param request 创建请求
     * @return 字典VO
     */
    DictVO create(DictCreateDTO request);

    /**
     * 更新字典
     * @param request 更新请求
     * @return 字典VO
     */
    DictVO update(DictUpdateDTO request);

    /**
     * 删除字典
     * @param id 字典ID
     */
    void delete(Long id);

    /**
     * 查询字典扁平选项
     * @param queryDTO 选项查询参数
     * @return 选项列表
     */
    List<OptionVO> listOptions(DictOptionQueryDTO queryDTO);

    /**
     * 查询字典树形选项
     * @param queryDTO 选项查询参数
     * @return 树形选项列表
     */
    List<TreeOptionVO> treeOptions(DictOptionQueryDTO queryDTO);

    /**
     * 根据字典ID查询详情列表
     * @param id 字典ID
     * @return 字典详情列表
     */
    List<DictDetailVO> getDetailsById(Long id);
}
