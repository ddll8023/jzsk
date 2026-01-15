package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("DGQ")
public class Dgq {
    @TableId(value = "TM")
    @JsonProperty("tm")
    private String TM;

    @TableField("DGQ_M1_Ia")
    @JsonProperty("dgq_M1_Ia")
    private Double DGQ_M1_Ia;
    @TableField("DGQ_M1_Ib")
    @JsonProperty("dgq_M1_Ib")
    private Double DGQ_M1_Ib;
    @TableField("DGQ_M1_Ic")
    @JsonProperty("dgq_M1_Ic")
    private Double DGQ_M1_Ic;
    @TableField("DGQ_M1_Ua")
    @JsonProperty("dgq_M1_Ua")
    private Double DGQ_M1_Ua;
    @TableField("DGQ_M1_Ub")
    @JsonProperty("dgq_M1_Ub")
    private Double DGQ_M1_Ub;
    @TableField("DGQ_M1_Uc")
    @JsonProperty("dgq_M1_Uc")
    private Double DGQ_M1_Uc;
    @TableField("DGQ_M1_Uab")
    @JsonProperty("dgq_M1_Uab")
    private Double DGQ_M1_Uab;
    @TableField("DGQ_M1_Ubc")
    @JsonProperty("dgq_M1_Ubc")
    private Double DGQ_M1_Ubc;
    @TableField("DGQ_M1_Uca")
    @JsonProperty("dgq_M1_Uca")
    private Double DGQ_M1_Uca;
    @TableField("DGQ_M1_KD")
    @JsonProperty("dgq_M1_KD")
    private Double DGQ_M1_KD;
    @TableField("DGQ_M1_KDSD")
    @JsonProperty("dgq_M1_KDSD")
    private Double DGQ_M1_KDSD;
} 