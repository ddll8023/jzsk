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
@TableName("wr_mp_q_r")
public class FlowCollector implements Serializable {
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
     * 瞬时流量
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "MP_Q")
    private Double mpQ;

    /**
     * 累计流量
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "ACC_W")
    private Double accW;

    /**
     * 总流量
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "IN_STP_Q")
    private Double inStpQ;

    /**
     * 总流量
     * @author admin
     * @date 2024/06/16 16:51
     */
    @TableField(value = "ACC_PQ_W")
    private Double accPqW;

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
