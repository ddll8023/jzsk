package com.szy.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.common.vo.WaterLevelVO;
import com.szy.common.vo.WaterQualityVO;
import com.szy.entity.WaterLevel;
import com.szy.entity.WaterQuality;

import java.time.LocalDate;
import java.util.List;

public interface WaterQualityService extends IService<WaterQuality> {
    /**
     * 根据日期和监测点对水位进行可视化
     * @param start
     * @param end
     * @param position
     * @return java.util.List<java.lang.Double>
     * @author admin
     * @date 2024/06/16 22:38
     */
    WaterQualityVO getWaterQualityStatistics(DateTime start, DateTime end, String position);

    /**
     * 导出全部数据到excel文件
     * @return java.util.List<com.szy.entity.WaterLevel>
     * @author admin
     * @date 2024/06/19 11:26
     */
    List<WaterQuality> exportAll(DateTime start, DateTime end, String position);

    /**
     * 根据水质新增预警信息
     * @param waterQuality
     * @author admin
     * @date 2024/07/02 15:54
     */
    void getDeterInformation(WaterQuality waterQuality);

    void getDeterInformationYl(WaterQuality waterQuality);

    void syncQualityData();
}
