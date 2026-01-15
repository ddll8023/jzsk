package com.szy.external.dto;

public class DeviceVideoList {
    private long channelNo;
    private long id;
    private String productType;
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