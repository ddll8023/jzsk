package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * 流量监测
 * @author admin
 * @date 2024/06/16 16:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Flow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警信息id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 站点编码
     */
    @TableField(value = "MP_CD")
    @Excel(name = "站码" , orderNum = "1")
    @NotBlank(message = "站码")
    private String mpCd;

    /**
     * 监测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "TM")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "监测时间" , orderNum = "2")
    @NotBlank(message = "监测时间")
    private Date tm;

    /**
     * 瞬时流量
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "MP_Q")
    @Excel(name = "瞬时流量(m³/s)" , orderNum = "3")
    private Double mpQ;

    /**
     * 累计流量
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "ACC_W")
    @Excel(name = "累计流量" , orderNum = "4")
    private Double accW;

    /**
     *
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "IN_STP_Q")
    @Excel(name = "入库流量" , orderNum = "5")
    private Double inStpQ;

    /**
     *
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "ACC_PQ_W")
    @Excel(name = "总入库流量" , orderNum = "6")
    private Double accPqW;

    /**
     * 创建时间
     */
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.mpCd).map(StringUtils::isBlank),
                this.tm == null
        ).contains(Boolean.TRUE);
    }
}
