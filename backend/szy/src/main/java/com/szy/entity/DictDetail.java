package com.szy.entity;

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
public class DictDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long id;

    @ExcelIgnore
    @TableField(value = "dict_id")
    private Long dictId;

    @ExcelIgnore
    @TableField(exist = false)
    private Dict dict;

    @NotBlank
    private String label;

    @NotBlank
    private String value;

    private Integer dictSort;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
