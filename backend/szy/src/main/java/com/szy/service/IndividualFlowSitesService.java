package com.szy.service;

import com.szy.entity.Impoundment;
import com.szy.entity.IndividualFlowSites;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-22
 */
public interface IndividualFlowSitesService extends IService<IndividualFlowSites> {
    void addIndividualFlowSites(IndividualFlowSites individualFlowSites);
    void updateIndividualFlowSites(IndividualFlowSites individualFlowSites);
    List<IndividualFlowSites> exportAll();
}
