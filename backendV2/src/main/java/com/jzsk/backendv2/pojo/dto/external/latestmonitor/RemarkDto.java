package com.jzsk.backendv2.pojo.dto.external.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GNSS位移监测备注信息
 * 对应外部平台返回的 remark 字段
 */
public class RemarkDto {

    /** 离线设备序列号列表 */
    @JsonProperty("offlineSn")
    private String[] offlineSn;

    public String[] getOfflineSn() {
        return offlineSn;
    }

    public void setOfflineSn(String[] offlineSn) {
        this.offlineSn = offlineSn;
    }
}
