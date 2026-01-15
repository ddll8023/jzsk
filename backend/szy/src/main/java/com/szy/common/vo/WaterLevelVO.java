package com.szy.common.vo;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterLevelVO implements Serializable {

    //日期时间，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
    private List<DateTime> dateTimeList;

    //设计洪水位，例如：406.0,1520.0,75.0
    private List<Double> designFloodList;

    //正常蓄水位
    private List<Double> normalStorageList;

    //死水位
    private List<Double> deadWaterList;

    //监测水位
    private List<Double> valueList;
}
