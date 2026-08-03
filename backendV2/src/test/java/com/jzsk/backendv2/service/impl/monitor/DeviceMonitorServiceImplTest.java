package com.jzsk.backendv2.service.impl.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.mapper.mcu.DataNewMapper;
import com.jzsk.backendv2.mapper.mcu.SensorPointMapper;
import com.jzsk.backendv2.mapper.monitor.StPptnHourMapper;
import com.jzsk.backendv2.mapper.monitor.StRiversRMapper;
import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceStatusVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceTypeStatusVO;
import com.jzsk.backendv2.service.external.DisplacementHistoryService;
import com.jzsk.backendv2.service.monitor.DeviceFaultRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceMonitorServiceImplTest {

    private static final int SEEPAGE_DEVICE_COUNT = 29;

    @Mock
    private DisplacementHistoryService displacementHistoryService;
    @Mock
    private DataNewMapper dataNewMapper;
    @Mock
    private SensorPointMapper sensorPointMapper;
    @Mock
    private StRiversRMapper stRiversRMapper;
    @Mock
    private StPptnHourMapper stPptnHourMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private DeviceFaultRecordService deviceFaultRecordService;
    @Mock
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @InjectMocks
    private DeviceMonitorServiceImpl deviceMonitorService;

    @Test
    void shouldReturnAllFixedDevicesWhenLatestDataQueryFails() {
        when(dataNewMapper.selectLatestForAllPoints())
                .thenThrow(new RuntimeException("pgsql unavailable"));

        DeviceTypeStatusVO result = deviceMonitorService.getSeepageStatus();

        assertEquals(SEEPAGE_DEVICE_COUNT, result.getStats().getTotal());
        assertEquals(0, result.getStats().getOnline());
        assertEquals(SEEPAGE_DEVICE_COUNT, result.getStats().getAbnormal());
        assertEquals(SEEPAGE_DEVICE_COUNT, result.getDevices().size());
        for (DeviceStatusVO device : result.getDevices()) {
            assertEquals("seepage", device.getType());
            assertEquals("abnormal", device.getStatus());
            assertEquals("网络故障", device.getDetail());
        }
        verify(sensorPointMapper, never()).selectAll();
        verify(deviceFaultRecordService, never()).processDeviceStatus(any(DeviceStatusVO.class));
        verify(threadPoolTaskScheduler).schedule(any(Runnable.class), any(Date.class));
    }

    @Test
    void shouldReturnAllFixedDevicesWhenPointMappingQueryFails() {
        when(dataNewMapper.selectLatestForAllPoints()).thenReturn(Collections.emptyList());
        when(sensorPointMapper.selectAll())
                .thenThrow(new RuntimeException("pgsql unavailable"));

        DeviceTypeStatusVO result = deviceMonitorService.getSeepageStatus();

        assertEquals(SEEPAGE_DEVICE_COUNT, result.getStats().getTotal());
        assertEquals(SEEPAGE_DEVICE_COUNT, result.getStats().getAbnormal());
        assertEquals("网络故障", result.getDevices().get(0).getDetail());
        verify(threadPoolTaskScheduler).schedule(any(Runnable.class), any(Date.class));
    }

    @Test
    void shouldKeepFixedDeviceCountWhenLatestDataIsEmpty() {
        when(dataNewMapper.selectLatestForAllPoints()).thenReturn(Collections.emptyList());
        when(sensorPointMapper.selectAll()).thenReturn(Collections.singletonList(point(1L, "P0100001")));

        DeviceTypeStatusVO result = deviceMonitorService.getSeepageStatus();

        assertEquals(SEEPAGE_DEVICE_COUNT, result.getStats().getTotal());
        assertEquals(SEEPAGE_DEVICE_COUNT, result.getStats().getAbnormal());
        for (DeviceStatusVO device : result.getDevices()) {
            assertEquals("采集异常", device.getDetail());
        }
    }

    @Test
    void shouldRecordAllFixedDevicesAfterNetworkRetries() {
        List<Runnable> scheduledTasks = new ArrayList<>();
        doAnswer(invocation -> {
            scheduledTasks.add((Runnable) invocation.getArguments()[0]);
            return null;
        }).when(threadPoolTaskScheduler).schedule(any(Runnable.class), any(Date.class));
        when(dataNewMapper.selectLatestForAllPoints())
                .thenThrow(new RuntimeException("pgsql unavailable"))
                .thenThrow(new RuntimeException("pgsql unavailable"))
                .thenThrow(new RuntimeException("pgsql unavailable"))
                .thenThrow(new RuntimeException("pgsql unavailable"));

        deviceMonitorService.getSeepageStatus();
        scheduledTasks.get(0).run();
        scheduledTasks.get(1).run();
        scheduledTasks.get(2).run();

        ArgumentCaptor<DeviceStatusVO> captor = ArgumentCaptor.forClass(DeviceStatusVO.class);
        verify(deviceFaultRecordService, times(SEEPAGE_DEVICE_COUNT))
                .processDeviceStatus(captor.capture());
        assertEquals(SEEPAGE_DEVICE_COUNT, captor.getAllValues().size());
        for (DeviceStatusVO device : captor.getAllValues()) {
            assertEquals("seepage", device.getType());
            assertEquals("abnormal", device.getStatus());
            assertEquals("网络故障", device.getDetail());
        }
    }

    private SensorPointEntity point(Long id, String name) {
        SensorPointEntity point = new SensorPointEntity();
        point.setId(id);
        point.setName(name);
        return point;
    }
}
