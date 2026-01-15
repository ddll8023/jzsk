package com.szy.service;

import com.szy.entity.IndividualFlowSites;
import com.szy.entity.IndividualPressureSites;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
public interface IndividualPressureSitesService extends IService<IndividualPressureSites> {
    void addIndividualPressureSites(IndividualPressureSites individualPressureSites);
    void updateIndividualPressureSites(IndividualPressureSites individualPressureSites);
    List<IndividualPressureSites> exportAll();
}
