package com.szy.common.criteria;

import lombok.Data;

/**
 * 公共查询类
 */
@Data
public class DictQueryCriteria {
    private String blurry;

    private Long offset;

    private Long size;
}
