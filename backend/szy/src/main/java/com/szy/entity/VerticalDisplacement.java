package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VerticalDisplacement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    private String projectName;

    private String monitoringType;

    private Float yAxisDisplacement;

    private LocalDateTime recordTime;

    private LocalDateTime uploadTime;


}
