package com.szy.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.common.vo.WaterLevelVO;
import com.szy.entity.WaterLevel;

import java.time.LocalDate;
import java.util.List;

public interface WaterLevelService extends IService<WaterLevel> {
    /**
     * 根据日期和监测点对水位进行可视化
     *
     * @param start
     * @param end
     * @param position
     * @return java.util.List<java.lang.Double>
     * @author admin
     * @date 2024/06/16 22:38
     */
    WaterLevelVO getWaterLevelStatistics(DateTime start, DateTime end, String position);

    /**
     * 导出全部数据到excel文件
     * @return java.util.List<com.szy.entity.WaterLevel>
     * @author admin
     * @date 2024/06/19 11:26
     */
    List<WaterLevel> exportAll(DateTime start, DateTime end, String position);

    void getDeterInformation(WaterLevel waterLevel);

    void syncLevelData();
}
