package com.jzsk.backendv2.task;

import java.util.Locale;
import java.util.Objects;

public final class ManagedTaskDefinition {

    private final TaskModule taskModule;
    private final Enum<?> taskCode;

    private ManagedTaskDefinition(TaskModule taskModule, Enum<?> taskCode) {
        this.taskModule = Objects.requireNonNull(taskModule, "taskModule不能为空");
        this.taskCode = Objects.requireNonNull(taskCode, "taskCode不能为空");
    }

    public static ManagedTaskDefinition of(TaskModule taskModule, Enum<?> taskCode) {
        return new ManagedTaskDefinition(taskModule, taskCode);
    }

    public TaskModule getTaskModule() {
        return taskModule;
    }

    public String getTaskKey() {
        return taskModule.name().toLowerCase(Locale.ROOT)
                + "."
                + taskCode.name().toLowerCase(Locale.ROOT).replace('_', '-');
    }
}
