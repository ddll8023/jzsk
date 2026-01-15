package com.szy.service.impl;

import com.szy.entity.DataNew;
import com.szy.mapper.DataNewMapper;
import com.szy.service.IDataNewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

/**
 * <p>
 * data_new 表 服务实现类
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Service
@DS("pgsql") // 假设 data_new 表在 pgsql 数据源中
public class DataNewServiceImpl extends ServiceImpl<DataNewMapper, DataNew> implements IDataNewService {

} 