package com.jzsk.backendv2.service.impl.report;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.report.DutyScheduleMapper;
import com.jzsk.backendv2.pojo.dto.report.DutyScheduleCreateDTO;
import com.jzsk.backendv2.pojo.dto.report.DutySchedulePageQueryDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyScheduleUpdateDTO;
import com.jzsk.backendv2.pojo.entity.report.DutyScheduleEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.report.DutyScheduleVO;
import com.jzsk.backendv2.service.report.DutyScheduleService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 值班安排服务实现类
 * 职责：提供值班安排的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DutyScheduleServiceImpl implements DutyScheduleService {

    private static final long MAX_PAGE_SIZE = 100L;

    private final DutyScheduleMapper dutyScheduleMapper;

    /**
     * 分页查询值班安排
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    @Override
    public PageResultVO<DutyScheduleVO> page(DutySchedulePageQueryDTO queryDTO) {
        DutySchedulePageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = dutyScheduleMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<DutyScheduleEntity> entities = dutyScheduleMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<DutyScheduleVO> voList = entities.stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    /**
     * 根据ID查询值班安排
     * @param id 值班安排ID
     * @return 值班安排VO
     */
    @Override
    public DutyScheduleVO getById(Long id) {
        Integer scheduleId = id == null ? null : Long.valueOf(id).intValue();
        DutyScheduleEntity entity = dutyScheduleMapper.selectById(scheduleId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "值班安排不存在");
        }
        return toVO(entity);
    }

    /**
     * 创建值班安排
     * @param request 创建请求
     * @return 值班安排VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DutyScheduleVO create(DutyScheduleCreateDTO request) {
        DutyScheduleEntity entity = new DutyScheduleEntity();
        entity.setDutyPerson(request.getDutyPerson());
        entity.setLeader(request.getLeader());
        entity.setDutyTime(request.getDutyTime());
        entity.setDutyPost(request.getDutyPost());
        entity.setCreateTime(LocalDateTime.now());

        dutyScheduleMapper.insert(entity);
        log.info("创建值班安排成功，id={}, 值班人员={}", entity.getDutyScheduleId(), entity.getDutyPerson());
        return toVO(entity);
    }

    /**
     * 更新值班安排
     * @param request 更新请求
     * @return 值班安排VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DutyScheduleVO update(DutyScheduleUpdateDTO request) {
        Integer scheduleId = request.getId() == null ? null : Long.valueOf(request.getId()).intValue();
        DutyScheduleEntity existing = dutyScheduleMapper.selectById(scheduleId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "值班安排不存在");
        }

        if (StringUtils.hasText(request.getDutyPerson())) {
            existing.setDutyPerson(request.getDutyPerson());
        }
        if (request.getLeader() != null) {
            existing.setLeader(request.getLeader());
        }
        if (request.getDutyTime() != null) {
            existing.setDutyTime(request.getDutyTime());
        }
        if (StringUtils.hasText(request.getDutyPost())) {
            existing.setDutyPost(request.getDutyPost());
        }

        dutyScheduleMapper.update(existing);
        log.info("更新值班安排成功，id={}", existing.getDutyScheduleId());
        return toVO(existing);
    }

    /**
     * 删除值班安排
     * @param id 值班安排ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Integer scheduleId = id == null ? null : Long.valueOf(id).intValue();
        DutyScheduleEntity existing = dutyScheduleMapper.selectById(scheduleId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "值班安排不存在");
        }

        dutyScheduleMapper.deleteById(scheduleId);
        log.info("删除值班安排成功，id={}", id);
    }

    /**
     * 批量删除值班安排
     * @param ids ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "ID列表不能为空");
        }

        dutyScheduleMapper.deleteByIds(ids);
        log.info("批量删除值班安排成功，数量={}", ids.size());
    }

    private DutySchedulePageQueryDTO normalizePageQuery(DutySchedulePageQueryDTO queryDTO) {
        DutySchedulePageQueryDTO normalized = new DutySchedulePageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        Long requestSize = queryDTO == null ? null : queryDTO.getSize();
        long size = requestSize == null || requestSize < 1L ? 10L : Math.min(requestSize, MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setStartDate(queryDTO.getStartDate());
            normalized.setEndDate(queryDTO.getEndDate());
        }
        return normalized;
    }

    private DutyScheduleVO toVO(DutyScheduleEntity entity) {
        return DutyScheduleVO.builder()
                .dutyScheduleId(entity.getDutyScheduleId())
                .dutyPerson(entity.getDutyPerson())
                .leader(entity.getLeader())
                .dutyTime(entity.getDutyTime())
                .dutyPost(entity.getDutyPost())
                .createTime(entity.getCreateTime())
                .build();
    }
}
