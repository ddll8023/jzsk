package com.szy.common.result;

import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页结果封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页结果")
public class PageResultVO<T> {

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "当前页码")
    private int page;

    @Schema(description = "每页大小")
    private int size;

    @Schema(description = "总页数")
    private int totalPages;

    /**
     * 从PageHelper的Page对象构建分页结果
     */
    public static <E, V> PageResultVO<V> of(Page<E> page, Function<E, V> converter) {
        List<V> list = page.stream().map(converter).collect(Collectors.toList());
        return new PageResultVO<>(
            list,
            page.getTotal(),
            page.getPageNum(),
            page.getPageSize(),
            page.getPages()
        );
    }

    /**
     * 从PageHelper的Page对象构建分页结果（无需转换）
     */
    public static <E> PageResultVO<E> of(Page<E> page) {
        return of(page, e -> e);
    }
}