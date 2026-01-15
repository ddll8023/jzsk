package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.InspectionRecords;
import com.szy.entity.MeasuringStation;
import com.szy.mapper.InspectionRecordsMapper;
import com.szy.mapper.MeasuringStationMapper;
import com.szy.service.InspectionRecordsService;
import com.szy.service.MeasuringStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.util.PictureUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@Service
@DS("gcdd")
public class MeasuringStationServiceImpl extends ServiceImpl<MeasuringStationMapper, MeasuringStation> implements MeasuringStationService {
    @Resource
    MeasuringStationMapper measuringStationMapper;
    @Resource
    MeasuringStationService measuringStationService;

    @Override
    @Transactional
    public void saveMeasuringStation(MeasuringStation measuringStation) {
        Date dateNow = new Date();
        String geom = "Point(" + measuringStation.getLongitude() + " " + measuringStation.getLatitude() + ")";
        measuringStationMapper.add(measuringStation.getCode(), measuringStation.getName(),
                measuringStation.getWaterName(),measuringStation.getRiverName(),measuringStation.getMonitorCode(),
                measuringStation.getAddressCode(), measuringStation.getEstablishDate(), measuringStation.getLongitude(),
                measuringStation.getLatitude(), measuringStation.getNote(),
                dateNow, dateNow, geom);
    }

    @Override
    public void updateMeasuringStation(MeasuringStation measuringStation) {
        measuringStationService.updateById(measuringStation);
        if(measuringStation.getLongitude() == null && measuringStation.getLatitude() == null) {
            return;
        }
        MeasuringStation update = measuringStationService.getById(measuringStation.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        measuringStationMapper.addPosition(update.getId(), geom);
    }

    @Override
    public List<MeasuringStation> exportAll() {
        List<MeasuringStation> measuringStations = measuringStationMapper.selectList(null);
        return measuringStations;
    }

    @Override
    public Map<String, String> getAllNamesAndCodes() {
        Map<String, String> map = new HashMap<>();
        List<String> nameList = measuringStationMapper.getAllNames();
        List<String> codeList = measuringStationMapper.getAllCodes();
        for (int i = 0; i < nameList.size(); i++) {
            map.put(nameList.get(i), codeList.get(i));
        }
        return map;
    }
}
