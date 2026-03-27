package com.jzsk.backendv2.service.impl.monitor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.mapper.monitor.GateMapper;
import com.jzsk.backendv2.pojo.dto.monitor.GateQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.monitor.GateDataVO;
import com.jzsk.backendv2.service.monitor.GateService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 闸门数据服务实现类
 * 职责：提供闸门数据查询功能
 * 数据源：zkxt（SQL Server）
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("zkxt")
public class GateServiceImpl implements GateService {

    private final GateMapper gateMapper;

    /** 合法的闸门编码集合 */
    private static final Set<String> VALID_GATE_CODES = new HashSet<>(Arrays.asList(
            "dgq", "dzdf", "qst", "xgq", "yhd"
    ));

    @Override
    public PageResultVO<GateDataVO> getGateDataByPage(String gateCode, long page, long size) {
        if (!isValidGateCode(gateCode)) {
            throw new BusinessException(400, "无效的闸门编码：" + gateCode);
        }
        GateQueryDTO queryDTO = new GateQueryDTO();
        queryDTO.setGateCode(gateCode);
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        return queryGateDataByPage(queryDTO);
    }

    @Override
    public PageResultVO<GateDataVO> getGateDataByTimeRange(GateQueryDTO queryDTO) {
        if (!isValidGateCode(queryDTO.getGateCode())) {
            throw new BusinessException(400, "无效的闸门编码：" + queryDTO.getGateCode());
        }
        // 规范化分页参数
        queryDTO.setPage(PageUtils.normalizePage(queryDTO.getPage()));
        queryDTO.setSize(PageUtils.normalizeSize(queryDTO.getSize()));
        return queryGateDataByPage(queryDTO);
    }

    @Override
    public boolean isValidGateCode(String gateCode) {
        return gateCode != null && VALID_GATE_CODES.contains(gateCode.toLowerCase());
    }

    /**
     * 分页查询闸门数据
     */
    private PageResultVO<GateDataVO> queryGateDataByPage(GateQueryDTO queryDTO) {
        String gateCode = queryDTO.getGateCode();
        String startTime = queryDTO.getStartTime();
        String endTime = queryDTO.getEndTime();
        long page = queryDTO.getPage();
        long size = queryDTO.getSize();

        log.info("分页查询闸门数据，编码：{}，页码：{}，每页：{}，开始时间：{}，结束时间：{}",
                gateCode, page, size, startTime, endTime);

        long total = countGateData(gateCode, startTime, endTime);
        if (total <= 0L) {
            return PageResultVO.empty(page, size);
        }

        long offset = (page - 1L) * size;
        List<GateDataVO> result = queryGateData(gateCode, startTime, endTime, offset, size);
        if (result.isEmpty()) {
            return PageResultVO.empty(page, size);
        }

        return PageUtils.buildPage(result, total, page, size);
    }

    /**
     * 统计闸门数据总数
     */
    private long countGateData(String gateCode, String startTime, String endTime) {
        switch (gateCode.toLowerCase()) {
            case "dgq":
                return gateMapper.countDgq(startTime, endTime);
            case "dzdf":
                return gateMapper.countDzdf(startTime, endTime);
            case "qst":
                return gateMapper.countQst(startTime, endTime);
            case "xgq":
                return gateMapper.countXgq(startTime, endTime);
            case "yhd":
                return gateMapper.countYhd(startTime, endTime);
            default:
                throw new BusinessException(400, "无效的闸门编码：" + gateCode);
        }
    }

    /**
     * 根据闸门编码查询数据（分页）
     */
    private List<GateDataVO> queryGateData(String gateCode, String startTime, String endTime, long offset, long size) {
        switch (gateCode.toLowerCase()) {
            case "dgq":
                return gateMapper.selectDgq(startTime, endTime, offset, size);
            case "dzdf":
                return gateMapper.selectDzdf(startTime, endTime, offset, size);
            case "qst":
                return gateMapper.selectQst(startTime, endTime, offset, size);
            case "xgq":
                return gateMapper.selectXgq(startTime, endTime, offset, size);
            case "yhd":
                return gateMapper.selectYhd(startTime, endTime, offset, size);
            default:
                throw new BusinessException(400, "无效的闸门编码：" + gateCode);
        }
    }
}
