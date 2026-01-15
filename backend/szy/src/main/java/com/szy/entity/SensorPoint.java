package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 传感器测点表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sensor_point")
public class SensorPoint implements Serializable {
    @TableId("id")
    private Long id;
    private String name;
}
