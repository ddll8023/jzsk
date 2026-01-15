package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.InspectionRecords;
import com.szy.entity.WarningInformation;
import com.szy.mapper.InspectionRecordsMapper;
import com.szy.service.InspectionRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.util.AliOSSUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Service
@DS("gcdd")
public class InspectionRecordsServiceImpl extends ServiceImpl<InspectionRecordsMapper, InspectionRecords> implements InspectionRecordsService {

    @Resource
    InspectionRecordsMapper inspectionRecordsMapper;
    @Resource
    InspectionRecordsService inspectionRecordsService;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Override
    @Transactional
    public void saveInspectionRecords(InspectionRecords inspectionRecords){
        Date dateNow = new Date();
        String geom = "Point(" + inspectionRecords.getLongitude() + " " + inspectionRecords.getLatitude() + ")";
        inspectionRecordsMapper.add(inspectionRecords.getProject(), inspectionRecords.getLongitude()
                ,inspectionRecords.getLatitude(), inspectionRecords.getType(), inspectionRecords.getAbnormal()
                ,inspectionRecords.getSituation() ,inspectionRecords.getSolve()
                ,inspectionRecords.getImage(),inspectionRecords.getPerson()
                ,dateNow ,dateNow, dateNow, geom);
    }

    @Override
    @Transactional
    public void updateInspectionRecords(InspectionRecords inspectionRecords){
        if(inspectionRecords.getLongitude() == null && inspectionRecords.getLatitude() == null) {
            return;
        }
        inspectionRecordsService.updateById(inspectionRecords);
        String geom = "'Point(" + inspectionRecords.getLongitude() + " " + inspectionRecords.getLatitude() + ")'";
        inspectionRecordsMapper.addPosition(inspectionRecords.getId(), geom);
    }

    @Override
    public List<InspectionRecords> exportAll(String name) {
        QueryWrapper<InspectionRecords> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            wrapper.like("person", name);
        }
        wrapper.orderByDesc("date");
        List<InspectionRecords> inspectionRecords = inspectionRecordsMapper.selectList(wrapper);
        return inspectionRecords;
    }

    @Override
    public List<String> getAllProjects() {
        return inspectionRecordsMapper.getAllProjects();
    }
}
