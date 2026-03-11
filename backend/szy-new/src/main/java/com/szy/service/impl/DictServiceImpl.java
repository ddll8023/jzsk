package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.DictDetailMapper;
import com.szy.mapper.DictMapper;
import com.szy.pojo.dto.DictDTO;
import com.szy.pojo.dto.DictQueryDTO;
import com.szy.pojo.entity.Dict;
import com.szy.pojo.entity.DictDetail;
import com.szy.pojo.vo.DictVO;
import com.szy.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典服务实现类
 */
@Service
@RequiredArgsConstructor
@DS("gcdd")
public class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;
    private final DictDetailMapper dictDetailMapper;

    @Override
    public PageInfo<DictVO> list(DictQueryDTO queryDTO) {
        int pageNum = queryDTO.getCurrentPage();
        int pageSize = queryDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);

        List<Dict> dictList = dictMapper.list(queryDTO.getBlurry(),
                (pageNum - 1) * pageSize, pageSize);
        List<DictVO> dictVOList = dictList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 查询总数
        long total = dictMapper.count(queryDTO.getBlurry());
        PageInfo<DictVO> pageInfo = new PageInfo<>(dictVOList);
        pageInfo.setTotal(total);
        return pageInfo;
    }

    @Override
    public DictVO getById(Long id) {
        Dict dict = dictMapper.findById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }
        return convertToVO(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictVO save(DictDTO dto) {
        // 检查名称是否已存在
        Dict existDict = dictMapper.findByName(dto.getName());
        if (existDict != null) {
            throw new BusinessException("该字典名称已存在");
        }

        Dict dict = new Dict();
        dict.setName(dto.getName());
        dict.setDescription(dto.getDescription());
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());

        dictMapper.insert(dict);
        return convertToVO(dict);
    }

    @Override
    public DictVO update(DictDTO dto) {
        Dict dict = dictMapper.findById(dto.getId());
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 检查名称是否与其他字典重复
        if (dto.getName() != null && !dto.getName().equals(dict.getName())) {
            Dict existDict = dictMapper.findByName(dto.getName());
            if (existDict != null) {
                throw new BusinessException("该字典名称已存在");
            }
        }

        dict.setName(dto.getName());
        dict.setDescription(dto.getDescription());
        dict.setUpdateTime(new Date());

        dictMapper.update(dict);
        return convertToVO(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        Dict dict = dictMapper.findById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }
        // 删除字典详情
        dictDetailMapper.deleteByDictId(id);
        // 删除字典
        dictMapper.deleteById(id);
    }

    @Override
    public List<String> findAllNames() {
        return dictMapper.findAllNames();
    }

    @Override
    public List<DictVO> findByDictName(String name) {
        Dict dict = dictMapper.findByDictName(name);
        if (dict == null) {
            return null;
        }
        List<DictDetail> details = dictDetailMapper.findByDictId(dict.getId());
        return details.stream()
                .map(detail -> {
                    DictVO vo = new DictVO();
                    vo.setId(detail.getId());
                    vo.setName(detail.getLabel());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    private DictVO convertToVO(Dict dict) {
        DictVO vo = new DictVO();
        vo.setId(dict.getId());
        vo.setName(dict.getName());
        vo.setDescription(dict.getDescription());
        vo.setCreateTime(dict.getCreateTime());
        vo.setUpdateTime(dict.getUpdateTime());
        return vo;
    }
}
