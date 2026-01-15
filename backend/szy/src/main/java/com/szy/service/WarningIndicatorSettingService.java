package com.szy.service;

import com.szy.entity.WarningIndicatorSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
public interface WarningIndicatorSettingService extends IService<WarningIndicatorSetting> {
    WarningIndicatorSetting getByPosition(String position, String type);

    /**
     * 获取所有监测类型
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/08 19:48
     */
    List<String> getAllTypes();

    /**
     * 获取所有预警指标
     */
    List<WarningIndicatorSetting> getAll();
}
