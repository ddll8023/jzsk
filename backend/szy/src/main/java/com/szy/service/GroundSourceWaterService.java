package com.szy.service;

import com.szy.entity.GroundSourceWater;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Waterworks;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-03-21
 */
public interface GroundSourceWaterService extends IService<GroundSourceWater> {
    void addGroundSourceWater(GroundSourceWater groundSourceWater);
    void updateGroundSourceWater(GroundSourceWater groundSourceWater);
    List<GroundSourceWater> exportAll();
}
