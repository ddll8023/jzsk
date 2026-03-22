package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailUpdateDTO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;

public interface DictDetailService {

    DictDetailVO getById(Long id);

    DictDetailVO create(DictDetailCreateDTO request);

    DictDetailVO update(DictDetailUpdateDTO request);

    void delete(Long id);
}
