package com.szy.common.vo;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterQualityVO implements Serializable {

    //日期时间，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
    private List<Date> dateTimeList;

    //温度
    private List<Double> temperatureList;

    //浊度
    private List<Double> turbidityList;

    //ph
    private List<Double> phList;

    //电导率
    private List<Double> conductivityList;

    //溶解氧
    private List<Double> oxygenList;

    //氨氮
    private List<Double> nitrogenList;

    //化学需氧量
    private List<Double> codList;

    //余氯
    private List<Double> chlorineList;

}
