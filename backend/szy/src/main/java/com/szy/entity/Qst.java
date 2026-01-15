package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("QST")
public class Qst {
    @TableId(value = "TM")
    @JsonProperty("tm")
    private String TM;

    @TableField("QST_M1_Ia")
    @JsonProperty("qst_M1_Ia")
    private Double QST_M1_Ia;
    @TableField("QST_M1_Ib")
    @JsonProperty("qst_M1_Ib")
    private Double QST_M1_Ib;
    @TableField("QST_M1_Ic")
    @JsonProperty("qst_M1_Ic")
    private Double QST_M1_Ic;
    @TableField("QST_M1_Ua")
    @JsonProperty("qst_M1_Ua")
    private Double QST_M1_Ua;
    @TableField("QST_M1_Ub")
    @JsonProperty("qst_M1_Ub")
    private Double QST_M1_Ub;
    @TableField("QST_M1_Uc")
    @JsonProperty("qst_M1_Uc")
    private Double QST_M1_Uc;
    @TableField("QST_M1_Uab")
    @JsonProperty("qst_M1_Uab")
    private Double QST_M1_Uab;
    @TableField("QST_M1_Ubc")
    @JsonProperty("qst_M1_Ubc")
    private Double QST_M1_Ubc;
    @TableField("QST_M1_Uca")
    @JsonProperty("qst_M1_Uca")
    private Double QST_M1_Uca;
    @TableField("QST_M1_KD")
    @JsonProperty("qst_M1_KD")
    private Double QST_M1_KD;
    @TableField("QST_M1_KDSD")
    @JsonProperty("qst_M1_KDSD")
    private Double QST_M1_KDSD;
    @TableField("QST_M2_Ia")
    @JsonProperty("qst_M2_Ia")
    private Double QST_M2_Ia;
    @TableField("QST_M2_Ib")
    @JsonProperty("qst_M2_Ib")
    private Double QST_M2_Ib;
    @TableField("QST_M2_Ic")
    @JsonProperty("qst_M2_Ic")
    private Double QST_M2_Ic;
    @TableField("QST_M2_Ua")
    @JsonProperty("qst_M2_Ua")
    private Double QST_M2_Ua;
    @TableField("QST_M2_Ub")
    @JsonProperty("qst_M2_Ub")
    private Double QST_M2_Ub;
    @TableField("QST_M2_Uc")
    @JsonProperty("qst_M2_Uc")
    private Double QST_M2_Uc;
    @TableField("QST_M2_Uab")
    @JsonProperty("qst_M2_Uab")
    private Double QST_M2_Uab;
    @TableField("QST_M2_KD")
    @JsonProperty("qst_M2_KD")
    private Double QST_M2_KD;
    @TableField("QST_M2_KDSD")
    @JsonProperty("qst_M2_KDSD")
    private Double QST_M2_KDSD;
} 