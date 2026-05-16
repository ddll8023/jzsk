package com.jzsk.backendv2.task.sync;

import com.jzsk.backendv2.service.monitor.DeviceFaultRecordService;
import com.jzsk.backendv2.task.AbstractManagedTask;
import com.jzsk.backendv2.task.ManagedTaskDefinition;
import com.jzsk.backendv2.task.TaskModule;
import com.jzsk.backendv2.task.TaskSwitchDecider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 设备到报情况历史数据保留期清理任务
 * 功能：每天清理两周以前的设备到报情况记录和事件明细
 * 触发方式：@Scheduled(cron = "0 10 2 * * ?")
 * 开关控制：jzsk.v2.task.task-switches.sync.device-fault-retention-clean
 */
@Slf4j
@Component
public class DeviceFaultRetentionCleanTask extends AbstractManagedTask {

    private static final int RETENTION_DAYS = 14;

    private static final ManagedTaskDefinition TASK_DEFINITION =
            ManagedTaskDefinition.of(TaskModule.SYNC, SyncTaskCode.DEVICE_FAULT_RETENTION_CLEAN);

    private final DeviceFaultRecordService deviceFaultRecordService;

    public DeviceFaultRetentionCleanTask(TaskSwitchDecider taskSwitchDecider,
                                         DeviceFaultRecordService deviceFaultRecordService) {
        super(taskSwitchDecider);
        this.deviceFaultRecordService = deviceFaultRecordService;
    }

    /**
     * 执行设备到报情况历史数据清理
     */
    @Scheduled(cron = "0 10 2 * * ?", zone = "${jzsk.v2.task.zone:Asia/Shanghai}")
    public void clean() {
        executeTask(TASK_DEFINITION, this::doClean);
    }

    private void doClean() {
        log.info("[DeviceFaultRetentionCleanTask] 定时任务开始执行");
        deviceFaultRecordService.cleanExpiredRecords(RETENTION_DAYS);
        log.info("[DeviceFaultRetentionCleanTask] 定时任务执行结束");
    }
}
