package com.szy.external.dto.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceInfoListDto {
    @JsonProperty("enable")
    private long enable;
    @JsonProperty("id")
    private long id;
    @JsonProperty("mouldId")
    private long mouldId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("remoteState")
    private long remoteState;
    @JsonProperty("sn")
    private String sn;

    public long getEnable() { return enable; }
    public void setEnable(long value) { this.enable = value; }

    public long getId() { return id; }
    public void setId(long value) { this.id = value; }

    public long getMouldId() { return mouldId; }
    public void setMouldId(long value) { this.mouldId = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String value) { this.protocol = value; }

    public long getRemoteState() { return remoteState; }
    public void setRemoteState(long value) { this.remoteState = value; }

    public String getSn() { return sn; }
    public void setSn(String value) { this.sn = value; }
} 