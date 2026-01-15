package com.szy.service;

import com.szy.entity.IndividualPressureSites;
import com.szy.entity.PumpStation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-20
 */
public interface PumpStationService extends IService<PumpStation> {
    void addPumpStation(PumpStation pumpStation);
    void updatePumpStation(PumpStation pumpStation);
    List<PumpStation> exportAll();
}
