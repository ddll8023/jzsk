package com.jzsk.backendv2.controller.monitor;

import com.jzsk.backendv2.pojo.dto.monitor.GateQueryDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.monitor.GateDataVO;
import com.jzsk.backendv2.service.monitor.GateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 闸门数据控制器
 * 用途：提供闸门数据查询的RESTful API接口
 * 统一收敛5个分散闸门接口为单一入口
 */
@Tag(name = "闸门管理", description = "闸门实时/历史数据查询接口")
@RestController
@RequestMapping("/api/gates")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class GateController {

    private final GateService gateService;

    /**
     * 根据闸门编码查询闸门数据
     * 统一入口，对应旧后端的 /zkxt/{gateCode}
     * @param gateCode 闸门编码（dgq-东干渠/dzdf-电站蝶阀/qst-取水塔/xgq-西干渠/yhd-溢洪道）
     * @return 闸门数据列表
     */
    @Operation(
            summary = "查询闸门数据",
            description = "根据闸门编码查询闸门实时/历史数据，支持dgq(东干渠)、dzdf(电站蝶阀)、qst(取水塔)、xgq(西干渠)、yhd(溢洪道)"
    )
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{gateCode}")
    public ResponseEntity<ApiResult<List<GateDataVO>>> getGateData(
            @Parameter(description = "闸门编码", required = true, example = "dgq")
            @PathVariable String gateCode) {
        List<GateDataVO> data = gateService.getGateData(gateCode);
        return ResponseEntity.ok(ApiResult.success(data, "查询成功"));
    }

    /**
     * 根据闸门编码和时间范围查询闸门数据
     * @param gateCode 闸门编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 闸门数据列表
     */
    @Operation(
            summary = "根据时间范围查询闸门数据",
            description = "根据闸门编码和时间范围查询闸门历史数据"
    )
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{gateCode}/time-range")
    public ResponseEntity<ApiResult<List<GateDataVO>>> getGateDataByTimeRange(
            @Parameter(description = "闸门编码", required = true, example = "dgq")
            @PathVariable String gateCode,
            @Parameter(description = "开始时间", required = false, example = "2024-01-01 00:00:00")
            @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间", required = false, example = "2024-01-02 00:00:00")
            @RequestParam(required = false) String endTime) {
        GateQueryDTO queryDTO = new GateQueryDTO();
        queryDTO.setGateCode(gateCode);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        List<GateDataVO> data = gateService.getGateDataByTimeRange(queryDTO);
        return ResponseEntity.ok(ApiResult.success(data, "查询成功"));
    }
}
