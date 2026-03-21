package com.jzsk.backendv2.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractManagedTask {

    private final TaskSwitchDecider taskSwitchDecider;

    protected final void executeTask(ManagedTaskDefinition taskDefinition, Runnable runnable) {
        if (!taskSwitchDecider.isEnabled(taskDefinition)) {
            log.info("task skipped module={} taskKey={} reason=task disabled",
                    taskDefinition.getTaskModule(),
                    taskDefinition.getTaskKey());
            return;
        }

        long startAt = System.currentTimeMillis();
        log.info("task started module={} taskKey={}",
                taskDefinition.getTaskModule(),
                taskDefinition.getTaskKey());
        try {
            runnable.run();
            log.info("task completed module={} taskKey={} costMs={}",
                    taskDefinition.getTaskModule(),
                    taskDefinition.getTaskKey(),
                    System.currentTimeMillis() - startAt);
        } catch (Exception ex) {
            log.error("task failed module={} taskKey={} costMs={}",
                    taskDefinition.getTaskModule(),
                    taskDefinition.getTaskKey(),
                    System.currentTimeMillis() - startAt,
                    ex);
            throw ex;
        }
    }
}
