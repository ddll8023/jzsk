package com.szy.service;

import com.szy.entity.InspectionRecords;
import com.szy.entity.MeasuringItem;
import com.szy.entity.MeasuringStation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
public interface MeasuringItemService extends IService<MeasuringItem> {
    void saveMeasuringItem(MeasuringItem measuringItem);
    void updateMeasuringItem(MeasuringItem measuringItem);
    List<MeasuringItem> exportAll();
}
