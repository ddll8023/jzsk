package com.jzsk.backendv2.service.impl.monitor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.mapper.monitor.GateMapper;
import com.jzsk.backendv2.pojo.dto.monitor.GateQueryDTO;
import com.jzsk.backendv2.pojo.vo.monitor.GateDataVO;
import com.jzsk.backendv2.service.monitor.GateService;
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
    public List<GateDataVO> getGateData(String gateCode) {
        if (!isValidGateCode(gateCode)) {
            throw new BusinessException(400, "无效的闸门编码：" + gateCode);
        }
        return queryGateData(gateCode, null, null);
    }

    @Override
    public List<GateDataVO> getGateDataByTimeRange(GateQueryDTO queryDTO) {
        if (!isValidGateCode(queryDTO.getGateCode())) {
            throw new BusinessException(400, "无效的闸门编码：" + queryDTO.getGateCode());
        }
        return queryGateData(queryDTO.getGateCode(), queryDTO.getStartTime(), queryDTO.getEndTime());
    }

    @Override
    public boolean isValidGateCode(String gateCode) {
        return gateCode != null && VALID_GATE_CODES.contains(gateCode.toLowerCase());
    }

    /**
     * 根据闸门编码查询数据
     */
    private List<GateDataVO> queryGateData(String gateCode, String startTime, String endTime) {
        log.info("查询闸门数据，编码：{}，开始时间：{}，结束时间：{}", gateCode, startTime, endTime);

        List<GateDataVO> result;

        switch (gateCode.toLowerCase()) {
            case "dgq":
                result = gateMapper.selectDgq(startTime, endTime);
                break;
            case "dzdf":
                result = gateMapper.selectDzdf(startTime, endTime);
                break;
            case "qst":
                result = gateMapper.selectQst(startTime, endTime);
                break;
            case "xgq":
                result = gateMapper.selectXgq(startTime, endTime);
                break;
            case "yhd":
                result = gateMapper.selectYhd(startTime, endTime);
                break;
            default:
                throw new BusinessException(400, "无效的闸门编码：" + gateCode);
        }

        log.info("查询闸门数据完成，编码：{}，记录数：{}", gateCode, result.size());
        return result;
    }
}
