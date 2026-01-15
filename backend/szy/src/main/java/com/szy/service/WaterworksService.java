package com.szy.service;

import com.szy.entity.WaterDistributor;
import com.szy.entity.Waterworks;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-24
 */
public interface WaterworksService extends IService<Waterworks> {
    void addWaterworks(Waterworks waterworks);
    void updateWaterworks(Waterworks waterworks);
    List<Waterworks> exportAll();
}
