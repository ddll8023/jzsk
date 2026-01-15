package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
@TableName("YHD")
public class Yhd {
    @TableId(value = "TM")
    private String TM;

    @TableField("YHD_M1_Ia")
    private Float YHD_M1_Ia;
    @TableField("YHD_M1_Ib")
    private Float YHD_M1_Ib;
    @TableField("YHD_M1_Ic")
    private Float YHD_M1_Ic;
    @TableField("YHD_M1_Ua")
    private Float YHD_M1_Ua;
    @TableField("YHD_M1_Ub")
    private Float YHD_M1_Ub;
    @TableField("YHD_M1_Uc")
    private Float YHD_M1_Uc;
    @TableField("YHD_M1_Uab")
    private Float YHD_M1_Uab;
    @TableField("YHD_M1_Ubc")
    private Float YHD_M1_Ubc;
    @TableField("YHD_M1_Uca")
    private Float YHD_M1_Uca;
    @TableField("YHD_M1_KD")
    private Float YHD_M1_KD;
    @TableField("YHD_M1_KDSD")
    private Float YHD_M1_KDSD;
    @TableField("YHD_M2_Ia")
    private Float YHD_M2_Ia;
    @TableField("YHD_M2_Ib")
    private Float YHD_M2_Ib;
    @TableField("YHD_M2_Ic")
    private Float YHD_M2_Ic;
    @TableField("YHD_M2_Ua")
    private Float YHD_M2_Ua;
    @TableField("YHD_M2_Ub")
    private Float YHD_M2_Ub;
    @TableField("YHD_M2_Uc")
    private Float YHD_M2_Uc;
    @TableField("YHD_M2_Uab")
    private Float YHD_M2_Uab;
    @TableField("YHD_M2_Ubc")
    private Float YHD_M2_Ubc;
    @TableField("YHD_M2_Uca")
    private Float YHD_M2_Uca;
    @TableField("YHD_M2_KD")
    private Float YHD_M2_KD;
    @TableField("YHD_M2_KDSD")
    private Float YHD_M2_KDSD;
    @TableField("YHD_M3_Ia")
    private Float YHD_M3_Ia;
    @TableField("YHD_M3_Ib")
    private Float YHD_M3_Ib;
    @TableField("YHD_M3_Ic")
    private Float YHD_M3_Ic;
    @TableField("YHD_M3_Ua")
    private Float YHD_M3_Ua;
    @TableField("YHD_M3_Ub")
    private Float YHD_M3_Ub;
    @TableField("YHD_M3_Uc")
    private Float YHD_M3_Uc;
    @TableField("YHD_M3_Uab")
    private Float YHD_M3_Uab;
    @TableField("YHD_M3_Ubc")
    private Float YHD_M3_Ubc;
    @TableField("YHD_M3_Uca")
    private Float YHD_M3_Uca;
    @TableField("YHD_M3_KD")
    private Float YHD_M3_KD;
    @TableField("YHD_M3_KDSD")
    private Float YHD_M3_KDSD;
} 