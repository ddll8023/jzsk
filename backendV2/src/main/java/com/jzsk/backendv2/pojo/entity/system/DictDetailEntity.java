package com.jzsk.backendv2.pojo.entity.system;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DictDetailEntity {

    private Long id;

    private Long dictId;

    private String label;

    private String value;

    private Integer dictSort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
