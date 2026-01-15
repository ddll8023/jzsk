package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.common.criteria.DictQueryCriteria;
import com.szy.common.vo.DictVO;
import com.szy.entity.Dict;
import com.szy.mapper.DictDetailMapper;
import com.szy.mapper.DictMapper;
import com.szy.service.DictService;
import com.szy.util.PageResult;
import com.szy.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@DS("gcdd")
@RequiredArgsConstructor
public class DictImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Resource
    private DictMapper dictMapper;
    @Resource
    private DictDetailMapper dictDetailMapper;

    @Override
    public void create(Dict dict) {
        dictMapper.insert(dict);
    }

    @Override
    public List<String> getAllNames() {
        return dictMapper.getAllNames();
    }

    @Override
    public List<String> getKinds(String name) {
        return dictMapper.getAllLabels(name);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        dictMapper.deleteById(id);
        List<Long> ids = dictDetailMapper.selectDetailsById(id);
        if(!ids.isEmpty()) {
            dictDetailMapper.deleteBatchIds(ids);
        }
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(name)){
            queryWrapper.eq("name",name);
        }
        //找到名称对应的字典
        Dict dict = dictMapper.selectOne(queryWrapper);
        if(dict != null){
            //删除该名称对应的字典
            dictMapper.delete(queryWrapper);
            //通过dict_id找到下面的dict_detail_id
            List<Long> ids = dictDetailMapper.selectDetailsById(dict.getId());
            if(!ids.isEmpty()) {
                dictDetailMapper.deleteBatchIds(ids);
            }
        }
    }

    @Override
    public PageResult<Dict> queryAll(String blurry, Page<Dict> page) {
        DictQueryCriteria criteria = new DictQueryCriteria();
        criteria.setBlurry(blurry);
        criteria.setOffset(page.offset());
        criteria.setSize(page.getSize());
        List<Dict> dicts = dictMapper.queryAll(criteria);
        Long number = 1L;
        for (Dict dict : dicts) {
            dict.setNumber(page.offset() + number);
            number += 1;
        }
        Long total = dictMapper.countAll(criteria);
        return PageUtil.toPage(dicts,total);
    }

    @Override
    public List<DictVO> getLVByName(String name) {
        return dictMapper.getAllLabelValues(name);
    }
}
