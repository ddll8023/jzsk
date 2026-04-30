package com.jzsk.backendv2.service.impl.water;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.mapper.monitor.StPptnHourMapper;
import com.jzsk.backendv2.pojo.dto.water.HourlyRainfallPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.water.HourlyRainfallQueryDTO;
import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.water.HourlyRainfallVO;
import com.jzsk.backendv2.service.water.HourlyRainfallService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 小时雨量服务实现类
 * 职责: 提供小时雨量数据的查询功能
 * 遵循KISS原则: 方法简洁，职责单一
 * 数据源: dbo（SQL Server）- 直接读取实时监测数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("dbo")
public class HourlyRainfallServiceImpl implements HourlyRainfallService {

    private final StPptnHourMapper stPptnHourMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HourlyRainfallVO> getHourlyRainfallList(HourlyRainfallQueryDTO queryDTO) {
        log.info("查询小时雨量列表，查询条件: {}", queryDTO);

        // 参数校验
        if (queryDTO == null) {
            queryDTO = new HourlyRainfallQueryDTO();
        }

        List<StPptnHourEntity> entities;

        // 根据查询条件选择查询方式
        if (queryDTO.getStartDate() != null || queryDTO.getEndDate() != null) {
            // 按时间范围查询
            entities = stPptnHourMapper.selectByTimeRange(
                queryDTO.getStartDate(),
                queryDTO.getEndDate()
            );
        } else {
            // 查询所有数据
            entities = stPptnHourMapper.selectAll();
        }

        // Entity转VO
        List<HourlyRainfallVO> voList = entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());

        log.info("查询小时雨量列表成功，返回{}条记录", voList.size());
        return voList;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResultVO<HourlyRainfallVO> getHourlyRainfallPage(HourlyRainfallPageQueryDTO queryDTO) {
        log.info("分页查询小时雨量数据，查询条件: {}", queryDTO);

        // 参数归一化
        queryDTO = (HourlyRainfallPageQueryDTO) PageUtils.normalize(queryDTO);

        // 查询总数
        long total = stPptnHourMapper.countPage(
            queryDTO.getStartDate(),
            queryDTO.getEndDate()
        );

        if (total <= 0L) {
            return PageResultVO.empty(queryDTO.getPage(), queryDTO.getSize());
        }

        // 分页查询
        long offset = (queryDTO.getPage() - 1L) * queryDTO.getSize();
        List<StPptnHourEntity> entities = stPptnHourMapper.selectPage(
            queryDTO.getStartDate(),
            queryDTO.getEndDate(),
            offset,
            queryDTO.getSize()
        );

        if (entities.isEmpty()) {
            return PageResultVO.empty(queryDTO.getPage(), queryDTO.getSize());
        }

        // Entity转VO
        List<HourlyRainfallVO> voList = entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());

        log.info("分页查询小时雨量数据成功，总记录数: {}, 当前页记录数: {}", total, voList.size());
        return PageUtils.buildPage(voList, total, queryDTO.getPage(), queryDTO.getSize());
    }

    /**
     * Entity转VO
     * @param entity 实体对象
     * @return VO对象
     */
    private HourlyRainfallVO convertToVO(StPptnHourEntity entity) {
        if (entity == null) {
            return null;
        }
        return new HourlyRainfallVO(
            entity.getStcd(),
            entity.getTm(),
            entity.getDrp(),
            entity.getIntv(),
            entity.getPdr(),
            entity.getDyp(),
            entity.getWth()
        );
    }
}
