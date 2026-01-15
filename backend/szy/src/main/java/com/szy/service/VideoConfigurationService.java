package com.szy.service;

import com.szy.entity.VideoConfiguration;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
public interface VideoConfigurationService extends IService<VideoConfiguration> {
    /**
     * 获取监控设备的类型
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/06 20:53
     */
    List<String> getAllTypes();

    /**
     * 获取所有乡镇
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/06 21:30
     */
    List<String> getAllTowns();

    /**
     * 通过乡镇获取钢乡镇的所有村庄
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/06 21:32
     */
    List<String> getVillagesByTown(String town);

    List<Map<String, Object>> getTree();

    /**
     * 获取所有设备名称
     * @return
     */
    List<String> getAllNames();

    Map<String, String> getAllNamesAndCodes();
}
