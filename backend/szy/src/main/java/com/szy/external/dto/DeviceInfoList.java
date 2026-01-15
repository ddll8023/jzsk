package com.szy.external.dto;

public class DeviceInfoList {
    private long enable;
    private long id;
    private long mouldId;
    private String name;
    private String protocol;
    private long remoteState;
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