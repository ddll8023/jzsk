package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * <p>
 * data_new 表实体类
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("data_new")
public class DataNew implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    private OffsetDateTime time;

    private String pointId;

    private String originalData;

    private String type;

    private Long indexes;

    private Long projectId;

    private Integer alarmSign;

    private String resultData;

    private String paramData;

    private Integer state;

    private String alarmDetail;

    private String confirm;

    private OffsetDateTime createTime;

}
