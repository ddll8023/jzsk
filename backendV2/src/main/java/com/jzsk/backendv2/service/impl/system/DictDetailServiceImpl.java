package com.jzsk.backendv2.service.impl.system;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.DictDetailMapper;
import com.jzsk.backendv2.mapper.system.DictMapper;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.DictDetailEntity;
import com.jzsk.backendv2.pojo.entity.system.DictEntity;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;
import com.jzsk.backendv2.service.system.DictDetailService;
import com.jzsk.backendv2.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * 字典详情服务实现类
 * 职责：提供字典详情的CRUD功能
 * 遵循KISS原则：方法简洁，职责单一
 */
public class DictDetailServiceImpl implements DictDetailService {

    private final DictDetailMapper dictDetailMapper;
    private final DictMapper dictMapper;

    @Override
    /**
     * 根据ID查询字典详情
     * @param id 字典详情ID
     * @return 字典详情VO
     */
    public DictDetailVO getById(Long id) {
        DictDetailEntity entity = dictDetailMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典详情不存在");
        }
        return toDictDetailVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * 创建字典详情
     * @param request 创建请求
     * @return 字典详情VO
     */
    public DictDetailVO create(DictDetailCreateDTO request) {
        DictEntity dict = dictMapper.selectBaseById(request.getDictId());
        if (dict == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "所属字典不存在");
        }

        String label = ValidationUtils.requireNonBlank(request.getLabel(), "字典标签不能为空");
        ensureLabelUnique(request.getDictId(), label, null);

        DictDetailEntity entity = new DictDetailEntity();
        entity.setDictId(request.getDictId());
        entity.setLabel(label);
        entity.setValue(ValidationUtils.requireNonBlank(request.getValue(), "字典值不能为空"));
        entity.setDictSort(normalizeSort(request.getDictSort()));
        dictDetailMapper.insert(entity);
        log.info("创建字典详情成功，detailId={}, dictId={}", entity.getId(), entity.getDictId());
        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * 更新字典详情
     * @param request 更新请求
     * @return 字典详情VO
     */
    public DictDetailVO update(DictDetailUpdateDTO request) {
        DictDetailEntity existing = dictDetailMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典详情不存在");
        }

        String label = ValidationUtils.requireNonBlank(request.getLabel(), "字典标签不能为空");
        ensureLabelUnique(existing.getDictId(), label, existing.getId());

        existing.setLabel(label);
        existing.setValue(ValidationUtils.requireNonBlank(request.getValue(), "字典值不能为空"));
        existing.setDictSort(normalizeSort(request.getDictSort()));
        dictDetailMapper.update(existing);
        log.info("更新字典详情成功，detailId={}, dictId={}", existing.getId(), existing.getDictId());
        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * 删除字典详情
     * @param id 字典详情ID
     */
    public void delete(Long id) {
        DictDetailEntity existing = dictDetailMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典详情不存在");
        }

        dictDetailMapper.deleteById(id);
        log.info("删除字典详情成功，detailId={}, dictId={}", existing.getId(), existing.getDictId());
    }

    private void ensureLabelUnique(Long dictId, String label, Long excludeId) {
        if (dictDetailMapper.countByDictIdAndLabel(dictId, label, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "字典标签已存在");
        }
    }

    private Integer normalizeSort(Integer sort) {
        return sort == null ? 0 : sort;
    }

    private DictDetailVO toDictDetailVO(DictDetailEntity entity) {
        return DictDetailVO.builder()
                .id(entity.getId())
                .dictId(entity.getDictId())
                .label(entity.getLabel())
                .value(entity.getValue())
                .dictSort(entity.getDictSort())
                .build();
    }

}
