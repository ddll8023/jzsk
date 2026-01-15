package com.szy.util;

import com.szy.service.RiverCollectorService;
import com.szy.service.WaterLevelService;
import com.szy.service.WaterQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataSyncTask {
    @Autowired
    private WaterLevelService waterLevelService;
    @Autowired
    private WaterQualityService waterQualityService;
    @Autowired
    private RiverCollectorService riverCollectorService;


    /**
     * 5分钟读取一次水质数据和泵站数据
     */
    @Scheduled(fixedRate = 300000)
    public void synWaterQualityData() {
        waterQualityService.syncQualityData();
    }

    /**
     * 5分钟读取一次流量传感器数据，水位数据
     */
    @Scheduled(fixedRate = 300000)
    public void synflowData() {
        riverCollectorService.syncFlowData();
    }
}
