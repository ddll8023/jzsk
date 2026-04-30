package com.jzsk.backendv2.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractManagedTask {

    private static final ConcurrentMap<String, AtomicBoolean> TASK_RUNNING_FLAGS = new ConcurrentHashMap<>();

    private final TaskSwitchDecider taskSwitchDecider;

    protected final void executeTask(ManagedTaskDefinition taskDefinition, Runnable runnable) {
        if (!taskSwitchDecider.isEnabled(taskDefinition)) {
            log.info("task skipped module={} taskKey={} reason=task disabled",
                    taskDefinition.getTaskModule(),
                    taskDefinition.getTaskKey());
            return;
        }

        String taskKey = taskDefinition.getTaskKey();
        AtomicBoolean runningFlag = TASK_RUNNING_FLAGS.computeIfAbsent(taskKey, key -> new AtomicBoolean(false));
        if (!runningFlag.compareAndSet(false, true)) {
            log.warn("task skipped module={} taskKey={} reason=task already running",
                    taskDefinition.getTaskModule(),
                    taskKey);
            return;
        }

        long startAt = System.currentTimeMillis();
        log.info("task started module={} taskKey={}",
                taskDefinition.getTaskModule(),
                taskKey);
        try {
            runnable.run();
            log.info("task completed module={} taskKey={} costMs={}",
                    taskDefinition.getTaskModule(),
                    taskKey,
                    System.currentTimeMillis() - startAt);
        } catch (Exception ex) {
            log.error("task failed module={} taskKey={} costMs={}",
                    taskDefinition.getTaskModule(),
                    taskKey,
                    System.currentTimeMillis() - startAt,
                    ex);
            throw ex;
        } finally {
            runningFlag.set(false);
        }
    }
}
