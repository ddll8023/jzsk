package com.jzsk.backendv2.pojo.entity.system;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DictEntity {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<DictDetailEntity> dictDetails = new ArrayList<>();
}
