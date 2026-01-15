package com.szy.external.dto;

import java.util.List;

public class DisplacementHistoryResponse {
    private int code;
    private String msg;
    private List<DisplacementHistoryDto> data;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public List<DisplacementHistoryDto> getData() { return data; }
    public void setData(List<DisplacementHistoryDto> data) { this.data = data; }
} 