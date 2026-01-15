package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.common.vo.WarningCount;
import com.szy.entity.WarningInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Mapper
@DS("yjxx")
public interface WarningInformationMapper extends BaseMapper<WarningInformation> {

    /**
     * 根据id添加预警信息的坐标
     * @param id
     * @param geom
     */
    void addPosition(Long id, String geom);

    void add(String position, String project, String content, String type, String level, String status, BigDecimal longitude, BigDecimal latitude, Date startTime, Date overTime, String stayTime, Date createTime, Date updateTime, String geom);

    /**
     * 获取所有的监测对象
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/06/19 22:23
     */
    List<String> getAllTypes();

    /**
     * 统计不同的预警类型的数量
     *
     * @return java.util.List<java.lang.Integer>
     * @author admin
     * @date 2024/06/20 10:24
     */
    List<WarningCount> getWarningLevels(Date start, Date end, String status);
}
