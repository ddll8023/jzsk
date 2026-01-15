package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("XGQ")
public class Xgq {
    @TableId(value = "TM")
    @JsonProperty("tm")
    private String TM;

    @TableField("XGQ_M1_Ia")
    @JsonProperty("xgq_M1_Ia")
    private Double XGQ_M1_Ia;
    @TableField("XGQ_M1_Ib")
    @JsonProperty("xgq_M1_Ib")
    private Double XGQ_M1_Ib;
    @TableField("XGQ_M1_Ic")
    @JsonProperty("xgq_M1_Ic")
    private Double XGQ_M1_Ic;
    @TableField("XGQ_M1_Ua")
    @JsonProperty("xgq_M1_Ua")
    private Double XGQ_M1_Ua;
    @TableField("XGQ_M1_Ub")
    @JsonProperty("xgq_M1_Ub")
    private Double XGQ_M1_Ub;
    @TableField("XGQ_M1_Uc")
    @JsonProperty("xgq_M1_Uc")
    private Double XGQ_M1_Uc;
    @TableField("XGQ_M1_Uab")
    @JsonProperty("xgq_M1_Uab")
    private Double XGQ_M1_Uab;
    @TableField("XGQ_M1_Ubc")
    @JsonProperty("xgq_M1_Ubc")
    private Double XGQ_M1_Ubc;
    @TableField("XGQ_M1_Uca")
    @JsonProperty("xgq_M1_Uca")
    private Double XGQ_M1_Uca;
    @TableField("XGQ_M1_KD")
    @JsonProperty("xgq_M1_KD")
    private Double XGQ_M1_KD;
    @TableField("XGQ_M1_KDSD")
    @JsonProperty("xgq_M1_KDSD")
    private Double XGQ_M1_KDSD;
    @TableField("XGQ_M2_Ia")
    @JsonProperty("xgq_M2_Ia")
    private Double XGQ_M2_Ia;
    @TableField("XGQ_M2_Ib")
    @JsonProperty("xgq_M2_Ib")
    private Double XGQ_M2_Ib;
    @TableField("XGQ_M2_Ic")
    @JsonProperty("xgq_M2_Ic")
    private Double XGQ_M2_Ic;
    @TableField("XGQ_M2_Ua")
    @JsonProperty("xgq_M2_Ua")
    private Double XGQ_M2_Ua;
    @TableField("XGQ_M2_Ub")
    @JsonProperty("xgq_M2_Ub")
    private Double XGQ_M2_Ub;
    @TableField("XGQ_M2_Uc")
    @JsonProperty("xgq_M2_Uc")
    private Double XGQ_M2_Uc;
    @TableField("XGQ_M2_Uab")
    @JsonProperty("xgq_M2_Uab")
    private Double XGQ_M2_Uab;
    @TableField("XGQ_M2_KD")
    @JsonProperty("xgq_M2_KD")
    private Double XGQ_M2_KD;
    @TableField("XGQ_M2_KDSD")
    @JsonProperty("xgq_M2_KDSD")
    private Double XGQ_M2_KDSD;
} 