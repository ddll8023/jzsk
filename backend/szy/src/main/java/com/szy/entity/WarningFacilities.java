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
 * @since 2025-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WarningFacilities implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String facilityName;

    private String type;

    private String location;

    private String status;

    private String manager;

    private LocalDateTime lastUpdate;

    private LocalDateTime recordTime;


}
