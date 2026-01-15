package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
@TableName("DZDF")
public class Dzdf {
    @TableId(value = "TM")
    private String TM;

    @TableField("DZDF_M1_Ia")
    private Float DZDF_M1_Ia;
    @TableField("DZDF_M1_Ib")
    private Float DZDF_M1_Ib;
    @TableField("DZDF_M1_Ic")
    private Float DZDF_M1_Ic;
    @TableField("DZDF_M1_Ua")
    private Float DZDF_M1_Ua;
    @TableField("DZDF_M1_Ub")
    private Float DZDF_M1_Ub;
    @TableField("DZDF_M1_Uc")
    private Float DZDF_M1_Uc;
    @TableField("DZDF_M1_Uab")
    private Float DZDF_M1_Uab;
    @TableField("DZDF_M1_Ubc")
    private Float DZDF_M1_Ubc;
    @TableField("DZDF_M1_Uca")
    private Float DZDF_M1_Uca;
    @TableField("DZDF_M1_YW")
    private Float DZDF_M1_YW;
    @TableField("DZDF_M1_FIT")
    private Float DZDF_M1_FIT;
    @TableField("DZDF_M1_FIT_TOL")
    private java.math.BigDecimal DZDF_M1_FIT_TOL;
} 