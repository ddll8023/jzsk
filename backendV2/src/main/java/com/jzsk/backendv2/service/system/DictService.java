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

public interface DictService {

    PageResultVO<DictVO> page(DictPageQueryDTO queryDTO);

    DictVO getById(Long id);

    DictVO create(DictCreateDTO request);

    DictVO update(DictUpdateDTO request);

    void delete(Long id);

    List<OptionVO> listOptions(DictOptionQueryDTO queryDTO);

    List<TreeOptionVO> treeOptions(DictOptionQueryDTO queryDTO);

    List<DictDetailVO> getDetailsById(Long id);
}
