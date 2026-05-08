package com.jzsk.backendv2.controller.monitor;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.monitor.DeviceFaultPageQueryDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceFaultEventLogVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceFaultRecordVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceTypeStatusVO;
import com.jzsk.backendv2.service.monitor.DeviceFaultRecordService;
import com.jzsk.backendv2.service.monitor.DeviceMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备监控控制器
 * 用途: 提供设备状态检测和监控数据查询接口
 * 遵循KISS原则: 每个接口独立返回单一设备类型的数据，支持前端渐进式加载
 */
@Slf4j
@RestController
@RequestMapping("/api/device-monitor")
@Tag(name = "设备监控", description = "提供设备运行状态检测和监控功能")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "JWT")
public class DeviceMonitorController {

    private final DeviceMonitorService deviceMonitorService;

    private final DeviceFaultRecordService deviceFaultRecordService;

    /**
     * 获取GNSS设备状态
     */
    @Operation(summary = "获取GNSS设备状态", description = "查询GNSS地表位移设备的在线/离线/采集异常状态")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/gnss")
    public ResponseEntity<ApiResult<DeviceTypeStatusVO>> getGnssStatus() {
        DeviceTypeStatusVO result = deviceMonitorService.getGnssStatus();
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 获取雨水情设备状态
     */
    @Operation(summary = "获取雨水情设备状态", description = "查询雨水情设备的在线/离线/采集异常状态")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/rain")
    public ResponseEntity<ApiResult<DeviceTypeStatusVO>> getRainStatus() {
        DeviceTypeStatusVO result = deviceMonitorService.getRainStatus();
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 获取渗流渗压设备状态
     */
    @Operation(summary = "获取渗流渗压设备状态", description = "查询渗流渗压设备的在线/离线/采集异常状态")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/seepage")
    public ResponseEntity<ApiResult<DeviceTypeStatusVO>> getSeepageStatus() {
        DeviceTypeStatusVO result = deviceMonitorService.getSeepageStatus();
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 分页查询历史故障记录
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询历史故障记录", description = "分页查询设备故障记录，支持按设备类型、故障状态、处理状态、关键词、时间范围筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @PostMapping("/fault-record/page")
    public ResponseEntity<ApiResult<PageResultVO<DeviceFaultRecordVO>>> faultRecordPage(
            @Parameter(description = "分页查询参数", required = true)
            @Valid @RequestBody DeviceFaultPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(deviceFaultRecordService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询故障事件明细
     * 权限：登录即可访问
     */
    @Operation(summary = "查询故障事件明细", description = "根据故障主记录ID查询事件时间线")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/fault-record/{id}/events")
    public ResponseEntity<ApiResult<List<DeviceFaultEventLogVO>>> faultRecordEvents(
            @Parameter(description = "故障主记录ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(deviceFaultRecordService.getEvents(id), "查询成功"));
    }

    /**
     * 删除故障记录
     */
    @Operation(summary = "删除故障记录", description = "删除指定故障记录及其关联的事件明细（物理删除）")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/fault-record/delete")
    public ResponseEntity<ApiResult<Void>> deleteFaultRecord(
            @Parameter(description = "故障记录删除请求", required = true, example = "{\"id\": 1}")
            @Valid @RequestBody IdRequestDTO request) {
        deviceFaultRecordService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
