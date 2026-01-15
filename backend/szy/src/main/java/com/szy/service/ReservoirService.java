package com.szy.service;

import com.szy.entity.PumpStation;
import com.szy.entity.Reservoir;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-23
 */
public interface ReservoirService extends IService<Reservoir> {
    void addReservoir(Reservoir reservoir);
    void updateReservoir(Reservoir reservoir);
    List<Reservoir> exportAll();
    List<String> getAllReservoirs();

}
