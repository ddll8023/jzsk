package com.szy.entity;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pump implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 泵id(主键)
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 泵名称
     */
    @Excel(name = "泵名称" , orderNum = "1")
    private String name;

    /**
     * 泵名称
     */
    @Excel(name = "泵编码" , orderNum = "2")
    private String code;

    /**
     * 所属泵站
     */
    @Excel(name = "所属泵站" , orderNum = "3")
    private String pumpStationName;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "4")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "5")
    private BigDecimal latitude;

    /**
     * 泵状态
     */
    @Excel(name = "状态" , orderNum = "6")
    private String status;
}
