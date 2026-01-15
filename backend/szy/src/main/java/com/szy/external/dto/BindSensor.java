package com.szy.external.dto;

public class BindSensor {
    private String[] sensor;
    private long id;
    private String sn;

    public String[] getSensor() { return sensor; }
    public void setSensor(String[] value) { this.sensor = value; }

    public long getId() { return id; }
    public void setId(long value) { this.id = value; }

    public String getSn() { return sn; }
    public void setSn(String value) { this.sn = value; }
} 