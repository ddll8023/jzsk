package com.szy.service;

import com.szy.entity.PumpStation;
import com.szy.entity.WarningInformation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
public interface WarningInformationService extends IService<WarningInformation> {
    void addWarningInformation(WarningInformation warningInformation);
    void updateWarningInformation(WarningInformation warningInformation);
    List<WarningInformation> exportAll();
    /**
     * 统计共有多少种监测类型
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/01 21:27
     */
    List<String> getAllTypes();
    /**
     * 统计不同类型的预警数量共有多少次
     * @param start
     * @param end
     * @return java.util.List<java.lang.Integer>
     * @author admin
     * @date 2024/07/01 21:26
     */
    List<Integer> getWarningLevels(Date start, Date end, String status);

    /**
     * 判断过去30分钟内是否有故障信息
     * @param position
     * @param type
     * @param monitorTime
     * @return boolean
     * @author admin
     * @date 2024/07/01 21:38
     */
    boolean getLastThirty(String position, String type, Date monitorTime);
}
