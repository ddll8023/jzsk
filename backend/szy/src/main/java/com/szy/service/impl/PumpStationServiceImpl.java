package com.szy.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.PumpStation;
import com.szy.mapper.PumpStationMapper;
import com.szy.service.PumpStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-20
 */
@Service
@DS("gcdd")
public class PumpStationServiceImpl extends ServiceImpl<PumpStationMapper, PumpStation> implements PumpStationService {
    @Resource
    PumpStationMapper pumpStationMapper;
    @Resource
    PumpStationService pumpStationService;
    @Override
    @Transactional
    public void addPumpStation(PumpStation pumpStation) {
//        Date dateNow = new Date();
//        String geom = "Point(" + pumpStation.getLongitude() + " " + pumpStation.getLatitude() + ")";
//        pumpStationMapper.add(pumpStation.getWaterSupplyProject(), pumpStation.getCode(),
//                pumpStation.getName(), pumpStation.getType(), pumpStation.getCompany(), pumpStation.getLongitude(),
//                pumpStation.getLatitude(), pumpStation.getAddress(), pumpStation.getOperationMode(),
//                pumpStation.getNumber(), pumpStation.getDesignScale(), pumpStation.getInstalledCapacity(),
//                pumpStation.getLift(), pumpStation.getDate(), dateNow, dateNow, geom);
        pumpStationService.addPumpStation(pumpStation);
    }

    @Override
    public void updatePumpStation(PumpStation pumpStation) {
        pumpStationService.updateById(pumpStation);
//        if(pumpStation.getLongitude() == null && pumpStation.getLatitude() == null) {
//            return;
//        }
//        PumpStation update = pumpStationService.getById(pumpStation.getId());
//        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
//        Long id = pumpStation.getId();
//        pumpStationMapper.addPosition(id, geom);
    }

    public List<PumpStation> exportAll() {
        List<PumpStation> pumpStations = pumpStationMapper.selectList(null);
        return pumpStations;
    }
}
