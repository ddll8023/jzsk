package com.szy.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DynamicValue {
    @JsonProperty("hasConfig")
    private boolean hasConfig;
    @JsonProperty("enable_script")
    private int enableScript;
    @JsonProperty("offline_hour")
    private int offlineHour;

    public boolean getHasConfig() { return hasConfig; }
    public void setHasConfig(boolean value) { this.hasConfig = value; }

    public int getEnableScript() { return enableScript; }
    public void setEnableScript(int value) { this.enableScript = value; }

    public int getOfflineHour() { return offlineHour; }
    public void setOfflineHour(int value) { this.offlineHour = value; }
} 