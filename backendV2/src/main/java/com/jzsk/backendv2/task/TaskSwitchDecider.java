package com.jzsk.backendv2.task;

import com.jzsk.backendv2.config.TaskProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSwitchDecider {

    private final TaskProperties taskProperties;

    public boolean isEnabled(ManagedTaskDefinition taskDefinition) {
        Boolean taskSwitch = taskProperties.getTaskSwitches().get(taskDefinition.getTaskKey());
        if (taskSwitch != null) {
            return taskSwitch;
        }
        return isModuleEnabled(taskDefinition.getTaskModule());
    }

    private boolean isModuleEnabled(TaskModule taskModule) {
        switch (taskModule) {
            case WARNING:
                return taskProperties.isWarningEnabled();
            case SYNC:
                return taskProperties.isSyncEnabled();
            default:
                return false;
        }
    }

    public String getZone() {
        return taskProperties.getZone();
    }
}
