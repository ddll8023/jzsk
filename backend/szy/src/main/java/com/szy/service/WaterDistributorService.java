package com.szy.service;

import com.szy.entity.WarningInformation;
import com.szy.entity.WaterDistributor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-22
 */
public interface WaterDistributorService extends IService<WaterDistributor> {
    void addWaterDistributor(WaterDistributor waterDistributor);
    void updateWaterDistributor(WaterDistributor waterDistributor);
    List<WaterDistributor> exportAll();
}
