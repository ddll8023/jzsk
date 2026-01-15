package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.GroundSourceWater;
import com.szy.entity.Impoundment;
import com.szy.mapper.GroundSourceWaterMapper;
import com.szy.mapper.ImpoundmentMapper;
import com.szy.service.GroundSourceWaterService;
import com.szy.service.ImpoundmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@Service
@DS("gcdd")
public class ImpoundmentServiceImpl extends ServiceImpl<ImpoundmentMapper, Impoundment> implements ImpoundmentService {
    @Resource
    ImpoundmentMapper impoundmentMapper;
    @Resource
    ImpoundmentService impoundmentService;
    @Override
    @Transactional
    public void addImpoundment (Impoundment impoundment) {
        Date dateNow = new Date();
        String geom = "Point(" + impoundment.getLongitude() + " " + impoundment.getLatitude() + ")";
        impoundmentMapper.add(impoundment.getCompany(), impoundment.getName(), impoundment.getAddress(), impoundment.getLongitude(), impoundment.getLatitude(), impoundment.getDate(), impoundment.getManageUnit(), impoundment.getVolume(), impoundment.getControlWaterLevel(), impoundment.getResponsiblePerson(), impoundment.getPhone(), impoundment.getRtuCode(), impoundment.getNote(), dateNow, dateNow ,geom);
    }

    @Override
    @Transactional
    public void updateImpoundment(Impoundment impoundment) {
        impoundmentService.updateById(impoundment);
        if(impoundment.getLongitude() == null && impoundment.getLatitude() == null) {
            return;
        }
        Impoundment update = impoundmentService.getById(impoundment.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        Long id = impoundment.getId();
        impoundmentMapper.addPosition(id, geom);
    }

    @Override
    public List<Impoundment> exportAll() {
        List<Impoundment> impoundments = impoundmentMapper.selectList(null);
        return impoundments;
    }
}
