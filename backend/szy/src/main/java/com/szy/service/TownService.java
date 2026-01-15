package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Town;

import java.util.List;

public interface TownService extends IService<Town> {

    /**
     * 新增判重
     * @return
     */
    List<String> getAllNames();

    void create(Town town);

    List<Town> exportAll();
}
