package com.jzsk.backendv2.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "树形选项对象", description = "通用树形选项返回结构")
public class TreeOptionVO extends OptionVO {

    @Schema(description = "子节点")
    private List<TreeOptionVO> children = new ArrayList<>();

    public TreeOptionVO(String label, Object value) {
        super(label, value);
    }

    public TreeOptionVO(String label, Object value, List<TreeOptionVO> children) {
        super(label, value);
        this.children = children == null ? new ArrayList<>() : children;
    }

    public List<TreeOptionVO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeOptionVO> children) {
        this.children = children == null ? new ArrayList<>() : children;
    }
}
