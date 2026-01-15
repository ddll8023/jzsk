package com.szy.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DistributorDto implements Serializable {
    /**
     * 分水口编号
     */
    private Long id;
    private String name;
    private String county;
}
