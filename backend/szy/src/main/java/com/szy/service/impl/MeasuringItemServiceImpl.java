package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.InspectionRecords;
import com.szy.entity.MeasuringItem;
import com.szy.entity.MeasuringStation;
import com.szy.mapper.InspectionRecordsMapper;
import com.szy.mapper.MeasuringItemMapper;
import com.szy.mapper.MeasuringStationMapper;
import com.szy.service.InspectionRecordsService;
import com.szy.service.MeasuringItemService;
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
import java.util.List;

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
public class MeasuringItemServiceImpl extends ServiceImpl<MeasuringItemMapper, MeasuringItem> implements MeasuringItemService {
    @Resource
    MeasuringItemMapper measuringItemMapper;
    @Resource
    MeasuringItemService measuringItemService;

    @Override
    @Transactional
    public void saveMeasuringItem(MeasuringItem measuringItem) {
        Date dateNow = new Date();
        measuringItemMapper.add(measuringItem.getNumber(), measuringItem.getName(),
                measuringItem.getUnit(),dateNow, dateNow);
    }

    @Override
    public void updateMeasuringItem(MeasuringItem measuringItem) {
        measuringItemService.updateById(measuringItem);
    }

    @Override
    public List<MeasuringItem> exportAll() {
        List<MeasuringItem> measuringItems = measuringItemMapper.selectList(null);
        return measuringItems;
    }
}
