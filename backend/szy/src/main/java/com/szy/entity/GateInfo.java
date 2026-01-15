package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 闸门基本信息表
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 闸门名称
     */
    private String name;

    /**
     * 位置
     */
    private String location;

    /**
     * 状态
     */
    private String status;


}
