package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Line;

import java.util.List;


public interface LineService extends IService<Line> {

    List<Line> exportAll();

    boolean existsByName(String name);
}
