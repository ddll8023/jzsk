package com.szy.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto implements Serializable {
    private Long id;
    private String name;
    private String path;
    private List<MenuDto> children = new ArrayList<>();
}
