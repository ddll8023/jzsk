package com.szy.entity;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class Line implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 巡检记录id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 位置
     */
    @Excel(name = "管道名称" , orderNum = "1")
    @NotBlank(message = "管道名称不能为空")
    private String name;

    /**
     * 位置
     */
    @Excel(name = "管道类型" , orderNum = "2")
    @NotBlank(message = "管道类型不能为空")
    private String type;

    /**
     * 备注
     */
    @Excel(name = "备注" , orderNum = "3")
    private String note;


    @Excel(name = "管道坐标" , orderNum = "4")
    @NotBlank(message = "管道坐标不能为空")
    private String points;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // 新增一个方法，将原始字符串解析为 List<Map<String, Double>>
    public List<String> parsePoints() {
        List<String> formattedPoints = new ArrayList<>();
        // 去掉开头和结尾的中括号，并按 '}, {' 分割字符串
        String[] pointPairs = points.replace("[", "").replace("]", "").split("}, \\{");
//        String[] pointPairs = points.replace("[", "").replace("]", "").split("}, \\{");
        for (String pair : pointPairs) {
            // 去掉大括号并按逗号分割
            String[] coords = pair.replace("{", "").replace("}", "").split(",");
            Double longitude = Double.parseDouble(coords[0].trim());
            Double latitude = Double.parseDouble(coords[1].trim());
            // 构建使用冒号分隔的字符串
            String formattedPoint = "{ \"latitude\": " + latitude + ", \"longitude\": " + longitude + " }";
//            String formattedPoint = "{latitude:" + latitude + ", longitude:" + longitude + "}";
            formattedPoints.add(formattedPoint);
        }
        return formattedPoints;
    }

    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.points).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }
}
