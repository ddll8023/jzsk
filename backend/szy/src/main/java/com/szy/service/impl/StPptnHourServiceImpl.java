package com.szy.service.impl;

import com.szy.entity.StPptnHour;
import com.szy.mapper.StPptnHourMapper;
import com.szy.service.IStPptnHourService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 逐小时降雨量数据表 服务实现类
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Service
@DS("dbo")
public class StPptnHourServiceImpl extends ServiceImpl<StPptnHourMapper, StPptnHour> implements IStPptnHourService {

}
