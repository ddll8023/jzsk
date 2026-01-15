package com.szy.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.common.vo.FlowVO;
import com.szy.common.vo.WaterLevelVO;
import com.szy.entity.Flow;
import com.szy.entity.WaterLevel;

import java.time.LocalDate;
import java.util.List;

@DS("gcdd")
public interface FlowService extends IService<Flow> {
    /**
     * 根据日期和监测点对水位进行可视化
     *
     * @param start
     * @param end
     * @param mpCd
     * @return java.util.List<java.lang.Double>
     * @author admin
     * @date 2024/06/16 22:38
     */
    FlowVO getFlowStatistics(DateTime start, DateTime end, String mpCd);

    /**
     * 导出全部数据到excel文件
     * @return java.util.List<com.szy.entity.WaterLevel>
     * @author admin
     * @date 2024/06/19 11:26
     */
    List<Flow> exportAll(DateTime start, DateTime end, String mpCd);

    /**
     * 判断是否要根据流量信息添加预警信息
     * @param flow
     * @author admin
     * @date 2024/07/02 14:54
     */
    void getDeterInformation(Flow flow);
}
