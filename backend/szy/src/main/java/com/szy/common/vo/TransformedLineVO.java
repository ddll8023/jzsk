package com.szy.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformedLineVO {
    private String name; // 假设 Line 类还有 name 字段
    private List<CoordinateVO> coordinates;

    // 省略 getter 和 setter 方法
}
