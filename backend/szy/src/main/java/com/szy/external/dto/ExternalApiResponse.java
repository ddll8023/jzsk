package com.szy.external.dto;

public class ExternalApiResponse {
    private long code;
    private ExternalStationDto[] data;
    private String msg;
    private boolean success;

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public ExternalStationDto[] getData() { return data; }
    public void setData(ExternalStationDto[] value) { this.data = value; }

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }
} 