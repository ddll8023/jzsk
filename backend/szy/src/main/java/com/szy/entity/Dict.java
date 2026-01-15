package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long id;

    @ExcelIgnore
    @TableField(exist = false)
    private List<DictDetail> dictDetails;

    @ExcelIgnore
    @TableField(exist = false)
    private Long number;

    @Excel(name = "数据项名称", orderNum = "1")
    @NotBlank
    private String name;

    @Excel(name = "描述", orderNum = "2")
    private String description;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
