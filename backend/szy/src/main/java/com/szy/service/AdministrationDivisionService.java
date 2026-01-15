package com.szy.service;

import com.szy.entity.AdministrationDivision;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
public interface AdministrationDivisionService extends IService<AdministrationDivision> {
    List<AdministrationDivision> exportAll();

}
