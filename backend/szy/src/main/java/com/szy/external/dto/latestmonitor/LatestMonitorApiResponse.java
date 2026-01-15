package com.szy.external.dto.latestmonitor;

import com.szy.external.dto.latestmonitor.LatestMonitorStationDto;

public class LatestMonitorApiResponse {
    private long code;
    private LatestMonitorStationDto[] data;
    private String msg;
    private boolean success;

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public LatestMonitorStationDto[] getData() { return data; }
    public void setData(LatestMonitorStationDto[] value) { this.data = value; }

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }
} 