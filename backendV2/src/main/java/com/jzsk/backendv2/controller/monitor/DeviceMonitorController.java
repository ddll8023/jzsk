package com.jzsk.backendv2.controller.monitor;

import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceMonitorOverviewVO;
import com.jzsk.backendv2.service.monitor.DeviceMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备监控控制器
 * 用途: 提供设备状态检测和监控数据查询接口
 * 遵循KISS原则: 简单清晰的接口设计
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

    /**
     * 获取所有设备监控状态
     * 权限: 需要登录
     */
    @Operation(summary = "获取设备监控状态", description = "查询所有设备的在线/离线/采集异常状态")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/status")
    public ResponseEntity<ApiResult<DeviceMonitorOverviewVO>> getDeviceMonitorStatus() {
        DeviceMonitorOverviewVO result = deviceMonitorService.getDeviceMonitorStatus();
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }
}
