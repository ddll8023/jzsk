package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.Events;
import com.szy.mapper.EventsMapper;
import com.szy.service.EventsService;
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
@DS("gcdd")
public class EventsServiceImpl extends ServiceImpl<EventsMapper, Events> implements EventsService {

    @Resource
    private EventsMapper eventsMapper;

    @Override
    public List<Events> exportAll() {
        List<Events> eventsList = eventsMapper.selectList(null);
        return eventsList;
    }
}
