package com.szy.common.vo;

import cn.hutool.core.date.DateTime;
import com.szy.entity.Flow;
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
public class FlowVO implements Serializable {

    //日期时间，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
    private List<DateTime> dateTimeList;

    //监测流量
    private List<Double> valueList;

}
