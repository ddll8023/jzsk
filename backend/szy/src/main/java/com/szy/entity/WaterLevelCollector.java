package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.Decimal;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * 水位监测
 * @author admin
 * @date 2024/06/16 16:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wr_mp_z_r")
public class WaterLevelCollector implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点编码
     */
    @TableField(value = "MP_CD")
    private String mpCd;

    /**
     * 监测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableId(value = "TM")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    /**
     * 监测值
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "MP_Z")
    private Double mpZ;

    /**
     * 创建时间
     */
    @TableField(value = "SPE_REG_DATA")
    private Integer speRegData;

    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "TS")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ts;

}

