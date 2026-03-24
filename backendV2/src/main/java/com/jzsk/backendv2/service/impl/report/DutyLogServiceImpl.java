package com.jzsk.backendv2.service.impl.report;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.report.DutyLogMapper;
import com.jzsk.backendv2.pojo.dto.report.DutyLogCreateDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogUpdateDTO;
import com.jzsk.backendv2.pojo.entity.report.DutyLogEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.report.DutyLogVO;
import com.jzsk.backendv2.service.report.DutyLogService;
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
 * 值班日志服务实现类
 * 职责：提供值班日志的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DutyLogServiceImpl implements DutyLogService {

    private static final long MAX_PAGE_SIZE = 100L;

    private final DutyLogMapper dutyLogMapper;

    /**
     * 分页查询值班日志
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    @Override
    public PageResultVO<DutyLogVO> page(DutyLogPageQueryDTO queryDTO) {
        DutyLogPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = dutyLogMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<DutyLogEntity> entities = dutyLogMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<DutyLogVO> voList = entities.stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    /**
     * 根据ID查询值班日志
     * @param id 值班日志ID
     * @return 值班日志VO
     */
    @Override
    public DutyLogVO getById(Long id) {
        Integer logId = id == null ? null : Long.valueOf(id).intValue();
        DutyLogEntity entity = dutyLogMapper.selectById(logId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "值班日志不存在");
        }
        return toVO(entity);
    }

    /**
     * 创建值班日志
     * @param request 创建请求
     * @return 值班日志VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DutyLogVO create(DutyLogCreateDTO request) {
        DutyLogEntity entity = new DutyLogEntity();
        entity.setDutyDate(request.getDutyDate());
        entity.setWeather(request.getWeather());
        entity.setRainfall(request.getRainfall());
        entity.setLeader(request.getLeader());
        entity.setDayShiftPerson(request.getDayShiftPerson());
        entity.setNightShiftPerson(request.getNightShiftPerson());
        entity.setLogContent(request.getLogContent());
        entity.setFillTime(LocalDateTime.now());
        entity.setLogStatus(request.getLogStatus() != null ? request.getLogStatus() : "已填写");

        dutyLogMapper.insert(entity);
        log.info("创建值班日志成功，id={}, 值班日期={}", entity.getDutyLogId(), entity.getDutyDate());
        return toVO(entity);
    }

    /**
     * 更新值班日志
     * @param request 更新请求
     * @return 值班日志VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DutyLogVO update(DutyLogUpdateDTO request) {
        Integer logId = request.getId() == null ? null : Long.valueOf(request.getId()).intValue();
        DutyLogEntity existing = dutyLogMapper.selectById(logId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "值班日志不存在");
        }

        if (request.getDutyDate() != null) {
            existing.setDutyDate(request.getDutyDate());
        }
        if (request.getWeather() != null) {
            existing.setWeather(request.getWeather());
        }
        if (request.getRainfall() != null) {
            existing.setRainfall(request.getRainfall());
        }
        if (request.getLeader() != null) {
            existing.setLeader(request.getLeader());
        }
        if (request.getDayShiftPerson() != null) {
            existing.setDayShiftPerson(request.getDayShiftPerson());
        }
        if (request.getNightShiftPerson() != null) {
            existing.setNightShiftPerson(request.getNightShiftPerson());
        }
        if (request.getLogContent() != null) {
            existing.setLogContent(request.getLogContent());
        }
        if (request.getLogStatus() != null) {
            existing.setLogStatus(request.getLogStatus());
        }

        dutyLogMapper.update(existing);
        log.info("更新值班日志成功，id={}", existing.getDutyLogId());
        return toVO(existing);
    }

    /**
     * 删除值班日志
     * @param id 值班日志ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Integer logId = id == null ? null : Long.valueOf(id).intValue();
        DutyLogEntity existing = dutyLogMapper.selectById(logId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "值班日志不存在");
        }

        dutyLogMapper.deleteById(logId);
        log.info("删除值班日志成功，id={}", id);
    }

    /**
     * 批量删除值班日志
     * @param ids ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "ID列表不能为空");
        }

        dutyLogMapper.deleteByIds(ids);
        log.info("批量删除值班日志成功，数量={}", ids.size());
    }

    private DutyLogPageQueryDTO normalizePageQuery(DutyLogPageQueryDTO queryDTO) {
        DutyLogPageQueryDTO normalized = new DutyLogPageQueryDTO();
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

    private DutyLogVO toVO(DutyLogEntity entity) {
        return DutyLogVO.builder()
                .dutyLogId(entity.getDutyLogId())
                .dutyDate(entity.getDutyDate())
                .weather(entity.getWeather())
                .rainfall(entity.getRainfall())
                .leader(entity.getLeader())
                .dayShiftPerson(entity.getDayShiftPerson())
                .nightShiftPerson(entity.getNightShiftPerson())
                .logContent(entity.getLogContent())
                .fillTime(entity.getFillTime())
                .logStatus(entity.getLogStatus())
                .build();
    }
}
