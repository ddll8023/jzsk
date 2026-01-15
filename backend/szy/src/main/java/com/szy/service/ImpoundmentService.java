package com.szy.service;

import com.szy.entity.GroundSourceWater;
import com.szy.entity.Impoundment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@Mapper
public interface ImpoundmentService extends IService<Impoundment> {
    void addImpoundment(Impoundment impoundment);
    void updateImpoundment(Impoundment impoundment);
    List<Impoundment> exportAll();
}
