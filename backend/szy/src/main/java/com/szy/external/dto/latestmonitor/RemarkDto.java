package com.szy.external.dto.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemarkDto {
    @JsonProperty("offlineSn")
    private String[] offlineSn;

    public String[] getOfflineSn() { return offlineSn; }
    public void setOfflineSn(String[] value) { this.offlineSn = value; }
} 