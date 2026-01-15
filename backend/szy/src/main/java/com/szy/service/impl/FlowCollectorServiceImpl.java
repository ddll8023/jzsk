package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.FlowCollector;
import com.szy.mapper.FlowCollectorMapper;
import com.szy.service.FlowCollectorService;
import org.springframework.stereotype.Service;

@Service
@DS("collector")
public class FlowCollectorServiceImpl extends ServiceImpl<FlowCollectorMapper, FlowCollector> implements FlowCollectorService {

}
