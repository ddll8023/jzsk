package com.szy.service;

import com.szy.entity.StRiversR;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 河道水情表 服务类
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
public interface IStRiversRService extends IService<StRiversR> {

    /**
     * 分页查询河道水情数据
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    Page<StRiversR> listByPage(Integer page, Integer size);
}