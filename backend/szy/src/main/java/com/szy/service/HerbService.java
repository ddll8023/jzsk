package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Herb;

import java.util.List;

public interface HerbService extends IService<Herb> {
    List<Herb> exportAll();
}
