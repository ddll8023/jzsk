package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.VideoConfiguration;
import com.szy.entity.WarningIndicatorSetting;
import com.szy.mapper.WarningIndicatorSettingMapper;
import com.szy.service.WarningIndicatorSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Service
@DS("yjxx")
public class WarningIndicatorSettingServiceImpl extends ServiceImpl<WarningIndicatorSettingMapper, WarningIndicatorSetting> implements WarningIndicatorSettingService {
    @Autowired
    WarningIndicatorSettingMapper  warningIndicatorSettingMapper;
    @Override
    public WarningIndicatorSetting getByPosition(String position, String type) {
        List<WarningIndicatorSetting> warningIndicatorSettings = warningIndicatorSettingMapper.getByPosition(position,type);
        if (!warningIndicatorSettings.isEmpty()) {
            return warningIndicatorSettings.get(0);
        }
        return null;
    }

    @Override
    public List<String> getAllTypes() {
        return warningIndicatorSettingMapper.getAllTypes();
    }

    @Override
    public List<WarningIndicatorSetting> getAll() {
        return this.list();
    }
}
