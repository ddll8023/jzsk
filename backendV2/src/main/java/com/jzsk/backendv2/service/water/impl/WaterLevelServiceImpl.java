package com.jzsk.backendv2.service.water.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.mapper.monitor.StRiversRMapper;
import com.jzsk.backendv2.pojo.dto.water.WaterLevelPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.water.WaterLevelQueryDTO;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.water.WaterLevelVO;
import com.jzsk.backendv2.service.water.WaterLevelService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 水位服务实现类
 * 职责: 提供水位数据的查询功能
 * 遵循KISS原则: 方法简洁,职责单一
 * 数据源: dbo(SQL Server)- 直接读取实时监测数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("dbo")
public class WaterLevelServiceImpl implements WaterLevelService {

    private final StRiversRMapper stRiverRMapper;

    @Override
    public PageResultVO<WaterLevelVO> getWaterLevelPage(WaterLevelPageQueryDTO queryDTO) {
        int page = (int) queryDTO.getPage();
        int size = (int) queryDTO.getSize();
        log.info("分页查询水位数据,页码: {}, 每页大小: {}, 查询条件: {}", page, size, queryDTO);

        // 查询总数
        long total = stRiverRMapper.countPage(
            queryDTO != null ? queryDTO.getStcd() : null
        );

        if (total <= 0L) {
            return PageResultVO.empty(page, size);
        }

        // 分页查询
        long offset = (page - 1L) * size;
        List<StRiversREntity> entities = stRiverRMapper.selectPage(
            queryDTO != null ? queryDTO.getStcd() : null,
            offset,
            size
        );

        if (entities.isEmpty()) {
            return PageResultVO.empty(page, size);
        }

        // Entity转VO
        List<WaterLevelVO> voList = entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());

        log.info("分页查询水位数据成功,总记录数: {}, 当前页记录数: {}", total, voList.size());
        return PageUtils.buildPage(voList, total, page, size);
    }

    @Override
    public List<WaterLevelVO> getWaterLevelList(WaterLevelQueryDTO queryDTO) {
        log.info("查询水位数据列表,查询条件: {}", queryDTO);

        List<StRiversREntity> entities;

        // 根据查询条件选择查询方式
        if (queryDTO != null && (queryDTO.getStartDate() != null || queryDTO.getEndDate() != null)) {
            // 按时间范围查询
            entities = stRiverRMapper.selectByTimeRange(
                queryDTO.getStartDate(),
                queryDTO.getEndDate()
            );
        } else if (queryDTO != null && queryDTO.getStcd() != null && !queryDTO.getStcd().isEmpty()) {
            // 按测站编码查询
            entities = stRiverRMapper.selectByStcd(queryDTO.getStcd());
        } else {
            // 查询所有数据
            entities = stRiverRMapper.selectAll();
        }

        // Entity转VO
        List<WaterLevelVO> voList = entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());

        log.info("查询水位数据列表成功,返回{}条记录", voList.size());
        return voList;
    }

    /**
     * Entity转VO
     * @param entity 实体对象
     * @return VO对象
     */
    private WaterLevelVO convertToVO(StRiversREntity entity) {
        if (entity == null) {
            return null;
        }
        return new WaterLevelVO(
            entity.getStcd(),
            entity.getTm(),
            entity.getZ1(),
            entity.getQ1()
        );
    }
}
