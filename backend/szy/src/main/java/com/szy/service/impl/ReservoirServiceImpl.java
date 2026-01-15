package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.PumpStation;
import com.szy.entity.Reservoir;
import com.szy.mapper.PumpStationMapper;
import com.szy.mapper.ReservoirMapper;
import com.szy.service.PumpStationService;
import com.szy.service.ReservoirService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-23
 */
@Service
@DS("gcdd")
public class ReservoirServiceImpl extends ServiceImpl<ReservoirMapper, Reservoir> implements ReservoirService {
    @Resource
    ReservoirMapper reservoirMapper;
    @Resource
    ReservoirService reservoirService;
    @Override
    @Transactional
    public void addReservoir(Reservoir reservoir) {
        Date dateNow = new Date();
        String geom = "Point(" + reservoir.getLongitude() + " " + reservoir.getLatitude() + ")";
        reservoirMapper.add(reservoir.getWaterSupply(), reservoir.getCode(), reservoir.getName(), reservoir.getLongitude(), reservoir.getLatitude(), reservoir.getLocate(), reservoir.getRegistrationNumber(), reservoir.getAdministrationDivision(), reservoir.getLevel(), reservoir.getScale(), reservoir.getTotalStorageCapacity(), reservoir.getRegulatingStorageCapacity(), reservoir.getDeadStorage(), reservoir.getDesignFloodLevel(), reservoir.getNormalStorageLevel(), reservoir.getDeadWaterLevel(), reservoir.getDate(), reservoir.getManageUnit(), reservoir.getWaterSupplyArea(), dateNow, dateNow, geom);
    }

    @Override
    @Transactional
    public void updateReservoir(Reservoir reservoir) {
        reservoirService.updateById(reservoir);
        if(reservoir.getLongitude() == null && reservoir.getLatitude() == null) {
            return;
        }
        Reservoir update = reservoirService.getById(reservoir.getId());
        String geom = "Point(" + update.getLongitude() + " " + update.getLatitude() + ")";
        Long id = reservoir.getId();
        reservoirMapper.addPosition(id, geom);
    }

    @Override
    public List<Reservoir> exportAll() {
        List<Reservoir> reservoirs = reservoirMapper.selectList(null);
        return reservoirs;
    }

    /**
     * 获取所有的水库名称
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/06/18 20:06
     */
    @Override
    public List<String> getAllReservoirs() {
        List<String> reservoirs = reservoirMapper.getAllReservoirs();
        return reservoirs;
    }
}
