package com.jzsk.backendv2.service.monitor;

import com.jzsk.backendv2.pojo.dto.monitor.DeviceFaultPageQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceFaultEventLogVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceFaultRecordVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceStatusVO;

import java.util.List;

/**
 * 设备故障记录服务接口
 * 职责：提供设备故障记录的生命周期管理和查询功能
 */
public interface DeviceFaultRecordService {

    /**
     * 处理设备状态变化，维护故障记录生命周期
     * @param device 设备状态VO
     */
    void processDeviceStatus(DeviceStatusVO device);

    /**
     * 分页查询故障记录
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResultVO<DeviceFaultRecordVO> page(DeviceFaultPageQueryDTO queryDTO);

    /**
     * 查询故障事件明细列表
     * @param faultRecordId 故障主记录ID
     * @return 事件明细列表
     */
    List<DeviceFaultEventLogVO> getEvents(Long faultRecordId);

    /**
     * 删除故障记录（级联删除事件明细）
     * @param id 故障记录ID
     */
    void delete(Long id);
}
