package com.szy.service;

import com.szy.entity.InspectionRecords;
import com.szy.entity.MeasuringStation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
public interface MeasuringStationService extends IService<MeasuringStation> {
    void saveMeasuringStation(MeasuringStation measuringStation);
    void updateMeasuringStation(MeasuringStation measuringStation);
    List<MeasuringStation> exportAll();
    Map<String, String> getAllNamesAndCodes();
}
