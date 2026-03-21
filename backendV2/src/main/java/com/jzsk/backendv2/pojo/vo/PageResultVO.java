package com.jzsk.backendv2.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "分页结果", description = "统一分页响应格式")
public class PageResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "数据列表")
    private final List<T> list;

    @Schema(description = "总记录数")
    private final long total;

    @Schema(description = "当前页码")
    private final long page;

    @Schema(description = "每页大小")
    private final long size;

    @Schema(description = "总页数")
    private final long totalPages;

    public static <T> PageResultVO<T> of(List<T> list, long total, long page, long size) {
        return new PageResultVO<>(list, total, page, size, calculateTotalPages(total, size));
    }

    public static <T> PageResultVO<T> empty(long page, long size) {
        return new PageResultVO<>(Collections.emptyList(), 0L, page, size, 0L);
    }

    private static long calculateTotalPages(long total, long size) {
        if (total <= 0L || size <= 0L) {
            return 0L;
        }
        return (total + size - 1L) / size;
    }
}
