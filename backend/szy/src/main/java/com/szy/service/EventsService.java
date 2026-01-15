package com.szy.service;

import com.szy.entity.Events;
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
public interface EventsService extends IService<Events> {

    List<Events> exportAll();
}
