package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.common.lang.Result;
import com.szy.entity.Dict;
import com.szy.entity.DictDetail;
import com.szy.mapper.DictDetailMapper;
import com.szy.mapper.DictMapper;
import com.szy.service.DictDetailService;
import com.szy.service.DictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@DS("gcdd")
public class DictDetailImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {
    @Resource
    private DictDetailMapper dictDetailMapper;
    @Resource
    private DictService dictService;
    @Resource
    private DictMapper dictMapper;

    /**
     * 根据字典详情id删除字典详情信息、对应的字典信息（主要是根据村子删除镇子）
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        DictDetail dictDetail = dictDetailMapper.selectById(id);
        dictService.deleteByName(dictDetail.getLabel());
        dictDetailMapper.deleteById(id);
    }

    /**
     * 新增字典详情，同时判重
     *
     * @param dictDetail
     * @return
     */
    @Override
    public Boolean create(DictDetail dictDetail) {
        Long dictId = dictDetail.getDictId();
        Dict dict = dictService.getById(dictId);
        List<String> labels = dictMapper.getAllLabels(dict.getName());
        if(labels.contains(dictDetail.getLabel())){
            return false;
        }
        dictDetailMapper.insert(dictDetail);
        return true;
    }
}
