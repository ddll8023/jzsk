package com.szy.external.dto.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceVideoListDto {
    @JsonProperty("channelNo")
    private long channelNo;
    @JsonProperty("id")
    private long id;
    @JsonProperty("productType")
    private String productType;
    @JsonProperty("sn")
    private String sn;

    public long getChannelNo() { return channelNo; }
    public void setChannelNo(long value) { this.channelNo = value; }

    public long getId() { return id; }
    public void setId(long value) { this.id = value; }

    public String getProductType() { return productType; }
    public void setProductType(String value) { this.productType = value; }

    public String getSn() { return sn; }
    public void setSn(String value) { this.sn = value; }
} 