package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bb")
public class WaterQualityCollector {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 清水池水位
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double sw;

    /**
     * yl1
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double yl1;

    /**
     * yl2
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double yl2;

    /**
     * yl3
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double yl3;

    /**
     * yl4
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double yl4;

    /**
     * ph值
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double ph;

    /**
     * 余氯
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double yl;

    /**
     * 电导率
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double ddl;

    /**
     * 氨氮
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double ad;

    /**
     * 浊度
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double zd;

    /**
     * 溶解氧
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double rjy;

    /**
     * 化学需氧量
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double cod;

    /**
     * 水温
     * @author admin
     * @date 2024/06/16 16:51
     */
    private Double swt;

    /**
     * 泵站状态1
     */
    private Integer jy1Run1;

    /**
     * 泵站状态1
     */
    private Integer jy1Run2;

    /**
     * 泵站状态1
     */
    private Integer jy2Run1;

    /**
     * 泵站状态1
     */
    private Integer jy2Run2;

    /**
     * 泵站状态1
     */
    private Integer jy3Run1;

    /**
     * 泵站状态1
     */
    private Integer jy3Run2;

    /**
     * 泵站状态1
     */
    private Integer jy4Run1;

    /**
     * 泵站状态1
     */
    private Integer jy4Run2;

    /**
     * 泵站状态1
     */
    private Integer jy5Run1;

    /**
     * 泵站状态1
     */
    private Integer jy5Run2;

    /**
     * 监测时间
     */
    @Excel(name = "监测时间" , importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

}
