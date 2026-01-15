package com.szy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.common.vo.DictVO;
import com.szy.entity.Dict;
import com.szy.util.PageResult;

import java.util.List;

public interface DictService extends IService<Dict> {


    void create(Dict dict);

    List<String> getAllNames();

    List<String> getKinds(String name);

    void delete(Long id);

    void deleteByName(String name);

    PageResult<Dict> queryAll(String blurry, Page<Dict> page);

    List<DictVO> getLVByName(String name);
}
