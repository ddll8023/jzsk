package com.szy.service;

import com.szy.entity.WaterSupplyProject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-14
 */
public interface WaterSupplyProjectService extends IService<WaterSupplyProject> {
    List<WaterSupplyProject> exportAll();
}
