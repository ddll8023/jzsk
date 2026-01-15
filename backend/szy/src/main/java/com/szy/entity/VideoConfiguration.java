package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 视频配置
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 摄像头id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 类型
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * IP地址
     */
    @NotBlank(message = "IP地址不能为空")
    private String ip;

    /**
     * 端口号
     */
    @NotBlank(message = "端口号不能为空")
    private String port;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 二级地点
     */
    @NotBlank(message = "二级地点不能为空")
    private String village;

    /**
     * 村
     */
    @NotBlank(message = "编码不能为空")
    private String videoCode;

    /**
     * 村
     */
    @NotBlank(message = "镇不能为空")
    private String town;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;




}
