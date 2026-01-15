package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.FloatingBoat;

import java.util.List;

public interface FloatingBoatService extends IService<FloatingBoat> {
    List<FloatingBoat> exportAll();
}
