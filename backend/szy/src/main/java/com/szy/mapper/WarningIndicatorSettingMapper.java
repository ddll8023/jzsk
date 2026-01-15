package com.szy.mapper;

import com.szy.entity.WarningIndicatorSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
public interface WarningIndicatorSettingMapper extends BaseMapper<WarningIndicatorSetting> {

    /**
     * 根据位置获得当前预警指标设定信息
     *
     * @param position
     * @return com.szy.entity.WarningIndicatorSetting
     * @author admin
     * @date 2024/07/01 11:14
     */
    List<WarningIndicatorSetting> getByPosition(String position, String type);

    /**
     * 获取所有监测类型
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/08 19:49
     */
    List<String> getAllTypes();
}
