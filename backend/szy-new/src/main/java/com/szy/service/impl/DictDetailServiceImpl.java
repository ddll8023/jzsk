package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.DictDetailMapper;
import com.szy.mapper.DictMapper;
import com.szy.pojo.dto.DictDetailDTO;
import com.szy.pojo.entity.Dict;
import com.szy.pojo.entity.DictDetail;
import com.szy.pojo.vo.DictDetailVO;
import com.szy.service.DictDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典详情服务实现类
 */
@Service
@RequiredArgsConstructor
@DS("gcdd")
public class DictDetailServiceImpl implements DictDetailService {

    private final DictDetailMapper dictDetailMapper;
    private final DictMapper dictMapper;

    @Override
    public DictDetailVO getById(Long id) {
        DictDetail dictDetail = dictDetailMapper.findById(id);
        if (dictDetail == null) {
            throw new BusinessException("字典详情不存在");
        }
        return convertToVO(dictDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictDetailVO save(DictDetailDTO dto) {
        // 检查字典是否存在
        Dict dict = dictMapper.findById(dto.getDictId());
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 检查value是否已存在
        int count = dictDetailMapper.countByDictIdAndValue(dto.getDictId(), dto.getValue(), null);
        if (count > 0) {
            throw new BusinessException("该字典值已存在");
        }

        DictDetail dictDetail = new DictDetail();
        dictDetail.setDictId(dto.getDictId());
        dictDetail.setLabel(dto.getLabel());
        dictDetail.setValue(dto.getValue());
        dictDetail.setDictSort(dto.getDictSort() != null ? dto.getDictSort() : 0);
        dictDetail.setCreateTime(new Date());
        dictDetail.setUpdateTime(new Date());

        dictDetailMapper.insert(dictDetail);
        return convertToVO(dictDetail);
    }

    @Override
    public DictDetailVO update(DictDetailDTO dto) {
        DictDetail dictDetail = dictDetailMapper.findById(dto.getId());
        if (dictDetail == null) {
            throw new BusinessException("字典详情不存在");
        }

        // 检查value是否与其他详情重复
        int count = dictDetailMapper.countByDictIdAndValue(dto.getDictId(), dto.getValue(), dto.getId());
        if (count > 0) {
            throw new BusinessException("该字典值已存在");
        }

        dictDetail.setLabel(dto.getLabel());
        dictDetail.setValue(dto.getValue());
        dictDetail.setDictSort(dto.getDictSort() != null ? dto.getDictSort() : 0);
        dictDetail.setUpdateTime(new Date());

        dictDetailMapper.update(dictDetail);
        return convertToVO(dictDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        DictDetail dictDetail = dictDetailMapper.findById(id);
        if (dictDetail == null) {
            throw new BusinessException("字典详情不存在");
        }
        dictDetailMapper.deleteById(id);
    }

    @Override
    public List<DictDetailVO> findByDictId(Long dictId) {
        List<DictDetail> details = dictDetailMapper.findByDictId(dictId);
        return details.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private DictDetailVO convertToVO(DictDetail dictDetail) {
        DictDetailVO vo = new DictDetailVO();
        vo.setId(dictDetail.getId());
        vo.setDictId(dictDetail.getDictId());
        vo.setLabel(dictDetail.getLabel());
        vo.setValue(dictDetail.getValue());
        vo.setDictSort(dictDetail.getDictSort());
        vo.setCreateTime(dictDetail.getCreateTime());
        vo.setUpdateTime(dictDetail.getUpdateTime());
        return vo;
    }
}
