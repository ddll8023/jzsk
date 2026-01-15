package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("st_river_r")
public class RiverCollector implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableId(value = "TM")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    @TableField("STCD")
    private String stcd;

    @TableField("Z1")
    private Double z1;

    @TableField("Q1")
    private Double q1;

    @TableField("XSA")
    private Double xsa;

    @TableField("XSAVV1")
    private Double xsavv1;

    @TableField("XSMXV")
    private Double xsmxv;

    @TableField("FLWCHRCD")
    private String flwchrcd;

    @TableField("WPTN")
    private String wptn;

    @TableField("MSQMT")
    private String msqmt;

    @TableField("MSAMT")
    private String msammt;

    @TableField("MSVMT")
    private String msvmt;

    @TableField("Z2")
    private Double z2;

    @TableField("Z3")
    private Double z3;

    @TableField("Z4")
    private Double z4;

    @TableField("Q2")
    private Double q2;

    @TableField("Q3")
    private Double q3;

    @TableField("Q4")
    private Double q4;

    @TableField("XSAVV2")
    private Double xsavv2;

    @TableField("XSAVV3")
    private Double xsavv3;

    @TableField("XSAVV4")
    private Double xsavv4;

    @TableField("SL1")
    private Double sl1;

    @TableField("SL2")
    private Double sl2;

    @TableField("SL3")
    private Double sl3;

    @TableField("SL4")
    private Double sl4;
    
}
