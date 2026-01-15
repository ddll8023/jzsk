package com.szy.external.dto.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.szy.external.dto.latestmonitor.DeviceInfoListDto;
import com.szy.external.dto.latestmonitor.DeviceVideoListDto;
import com.szy.external.dto.latestmonitor.RemarkDto;

public class LatestMonitorStationDto {
    @JsonProperty("alarmLevel")
    private long alarmLevel;
    @JsonProperty("alarmState")
    private Long alarmState;
    @JsonProperty("bindDevice")
    private String bindDevice;
    @JsonProperty("collectTime")
    private String collectTime;
    @JsonProperty("deviceInfoList")
    private DeviceInfoListDto[] deviceInfoList;
    @JsonProperty("deviceVideoList")
    private DeviceVideoListDto[] deviceVideoList;
    @JsonProperty("h")
    private String h;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("icon3d")
    private String icon3d;
    @JsonProperty("image")
    private String image;
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("lon")
    private String lon;
    @JsonProperty("monDate")
    private String monDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("projectId")
    private long projectId;
    @JsonProperty("remark")
    private RemarkDto remark;
    @JsonProperty("stationId")
    private long stationId;
    @JsonProperty("stationMethods")
    private String stationMethods;
    @JsonProperty("stationName")
    private String stationName;
    @JsonProperty("stationType")
    private long stationType;
    @JsonProperty("stationTypeCode")
    private long stationTypeCode;
    @JsonProperty("stationDynamicValue")
    private String stationDynamicValue;
    @JsonProperty("workStatus")
    private long workStatus;
    @JsonProperty("bindSensor")
    private String bindSensor;

    public long getAlarmLevel() { return alarmLevel; }
    public void setAlarmLevel(long value) { this.alarmLevel = value; }

    public Long getAlarmState() { return alarmState; }
    public void setAlarmState(Long value) { this.alarmState = value; }

    public String getBindDevice() { return bindDevice; }
    public void setBindDevice(String value) { this.bindDevice = value; }

    public String getCollectTime() { return collectTime; }
    public void setCollectTime(String value) { this.collectTime = value; }

    public DeviceInfoListDto[] getDeviceInfoList() { return deviceInfoList; }
    public void setDeviceInfoList(DeviceInfoListDto[] value) { this.deviceInfoList = value; }

    public DeviceVideoListDto[] getDeviceVideoList() { return deviceVideoList; }
    public void setDeviceVideoList(DeviceVideoListDto[] value) { this.deviceVideoList = value; }

    public String getH() { return h; }
    public void setH(String value) { this.h = value; }

    public String getIcon() { return icon; }
    public void setIcon(String value) { this.icon = value; }

    public String getIcon3d() { return icon3d; }
    public void setIcon3d(String value) { this.icon3d = value; }

    public String getImage() { return image; }
    public void setImage(String value) { this.image = value; }

    public String getLat() { return lat; }
    public void setLat(String value) { this.lat = value; }

    public String getLon() { return lon; }
    public void setLon(String value) { this.lon = value; }

    public String getMonDate() { return monDate; }
    public void setMonDate(String value) { this.monDate = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public long getProjectId() { return projectId; }
    public void setProjectId(long value) { this.projectId = value; }

    public RemarkDto getRemark() { return remark; }
    public void setRemark(RemarkDto value) { this.remark = value; }

    public long getStationId() { return stationId; }
    public void setStationId(long value) { this.stationId = value; }

    public String getStationMethods() { return stationMethods; }
    public void setStationMethods(String value) { this.stationMethods = value; }

    public String getStationName() { return stationName; }
    public void setStationName(String value) { this.stationName = value; }

    public long getStationType() { return stationType; }
    public void setStationType(long value) { this.stationType = value; }

    public long getStationTypeCode() { return stationTypeCode; }
    public void setStationTypeCode(long value) { this.stationTypeCode = value; }

    public String getStationDynamicValue() { return stationDynamicValue; }
    public void setStationDynamicValue(String value) { this.stationDynamicValue = value; }

    public long getWorkStatus() { return workStatus; }
    public void setWorkStatus(long value) { this.workStatus = value; }

    public String getBindSensor() { return bindSensor; }
    public void setBindSensor(String value) { this.bindSensor = value; }
} 