package com.szy.external.dto;

import java.util.List;

public class DisplacementHistoryDto {
    private String collectTime;
    private Double azimuth;
    private String stationName;
    private List<DisplacementKeyValueDto> keyValues;
    private String stringCollectTime;
    private String deviceId;
    private String deviceSn;
    private Long stationId;

    public String getCollectTime() { return collectTime; }
    public void setCollectTime(String collectTime) { this.collectTime = collectTime; }
    public Double getAzimuth() { return azimuth; }
    public void setAzimuth(Double azimuth) { this.azimuth = azimuth; }
    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }
    public List<DisplacementKeyValueDto> getKeyValues() { return keyValues; }
    public void setKeyValues(List<DisplacementKeyValueDto> keyValues) { this.keyValues = keyValues; }
    public String getStringCollectTime() { return stringCollectTime; }
    public void setStringCollectTime(String stringCollectTime) { this.stringCollectTime = stringCollectTime; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDeviceSn() { return deviceSn; }
    public void setDeviceSn(String deviceSn) { this.deviceSn = deviceSn; }
    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }
} 