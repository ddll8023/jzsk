package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.common.vo.WarningCount;
import com.szy.entity.WarningInformation;
import com.szy.mapper.WarningInformationMapper;
import com.szy.service.WarningInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Service
@DS("yjxx")
public class WarningInformationServiceImpl extends ServiceImpl<WarningInformationMapper, WarningInformation> implements WarningInformationService {
    @Resource
    WarningInformationMapper warningInformationMapper;
    @Resource
    WarningInformationService warningInformationService;

    /**
     * 添加预警信息
     *
     * @param warningInformation
     */
    @Override
    public void addWarningInformation(WarningInformation warningInformation) {
        Date dateNow = new Date();
        String geom = "Point(" + warningInformation.getLongitude() + " " + warningInformation.getLatitude() + ")";
        //经纬度需要通过编写sql来增加进去，不能直接使用mybatis-plus
        warningInformationMapper.add(warningInformation.getPosition(), warningInformation.getProject(),
                warningInformation.getContent(), warningInformation.getType(),
                warningInformation.getLevel(), warningInformation.getStatus(),
                warningInformation.getLongitude(), warningInformation.getLatitude(),
                warningInformation.getStartTime(), warningInformation.getOverTime(),
                warningInformation.getStayTime(), dateNow, dateNow, geom);
    }

    @Override
    public void updateWarningInformation(WarningInformation warningInformation) {
        //首先修改除了point之外的东西（这个方法好像没有起到作用？）

        warningInformationService.updateById(warningInformation);
        if(warningInformation.getLongitude() == null && warningInformation.getLatitude() == null) {
            return;
        }
        //如果经纬度不为空，添加point
        WarningInformation update = warningInformationService.getById(warningInformation.getId());
        String geom = "'Point(" + update.getLongitude() + " " + update.getLatitude() + ")'";
        warningInformationMapper.addPosition(warningInformation.getId(), geom);
    }

    @Override
    public List<WarningInformation> exportAll() {
        List<WarningInformation> warningInformations = warningInformationMapper.selectList(null);
        return warningInformations;
    }

    /**
     * 获取所有的监测对象
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/06/19 22:22
     */
    @Override
    public List<String> getAllTypes() {
        List<String> types = warningInformationMapper.getAllTypes();
        return types;
    }

    @Override
    public List<Integer> getWarningLevels(Date start,Date end,String status) {
        List<WarningCount> warningCounts = warningInformationMapper.getWarningLevels(start,end,status);
        // 初始化一个数组来存储结果
        List<Integer> specificWarningCountsArray = new ArrayList<>();
        // 将一般预警和严重预警的数量放入数组
        int generalWarningCount = warningCounts.stream()
                .filter(wc -> "一般预警".equals(wc.getLevel()))
                .mapToInt(WarningCount::getNumber)
                .findFirst()
                .orElse(0); // 如果没有找到，使用0作为默认值

        int severeWarningCount = warningCounts.stream()
                .filter(wc -> "严重预警".equals(wc.getLevel()))
                .mapToInt(WarningCount::getNumber)
                .findFirst()
                .orElse(0); // 如果没有找到，使用0作为默认值

        specificWarningCountsArray.add(generalWarningCount);
        specificWarningCountsArray.add(severeWarningCount);
        return specificWarningCountsArray;
    }

    /**
     * 如果过去30分钟内不存在相同位置的预警信息，就返回true
     * @param position
     * @param type
     * @param monitorTime
     * @return boolean
     * @author admin
     * @date 2024/07/01 21:45
     */
    @Override
    public boolean getLastThirty(String position, String type, Date monitorTime) {
        int currentPage = 1;
        int pageSize = 5;
        // 将Date转换为LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(monitorTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime thirtyMinutesAgo = dateTime.minusMinutes(60);
        Date resultDate = Date.from(thirtyMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
        QueryWrapper<WarningInformation> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(type)) {
            wrapper.eq("type", type);
        }
        if (!StringUtils.isBlank(position)) {
            wrapper.like("position", position);
        }
        wrapper.between("start_time", resultDate, monitorTime); // 使用between代替gt和lt，更明确表示时间范围
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("start_time");
        Page<WarningInformation> page = new Page<>(currentPage, pageSize);
        Page<WarningInformation> warningInformationPage = warningInformationService.page(page, wrapper);
        if(warningInformationPage.getRecords().isEmpty()){
            return true;
        }
        return false;
    }
}
