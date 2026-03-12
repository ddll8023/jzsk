package com.szy.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页结果封装
 * 遵循规范：必须包含 list, total, page, size, totalPages（规范7.7.2）
 */
@Schema(name = "分页结果", description = "统一分页响应格式")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 静态方法快速构建（规范推荐）
     * @param list 数据列表
     * @param converter 转换函数
     * <R> 原始 @param类型
     * @param <V> 目标类型
     * @return 分页结果
     */
    public static <R, V> PageResultVO<V> restPage(List<R> list, Function<R, V> converter) {
        if (list == null || list.isEmpty()) {
            return new PageResultVO<>(Collections.emptyList(), 0, 0, 0, 0);
        }

        // 获取PageInfo中的分页信息
        com.github.pagehelper.PageInfo<R> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        List<V> convertList = list.stream()
                .map(converter)
                .collect(Collectors.toList());

        return new PageResultVO<>(
                convertList,
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getPages()
        );
    }

    /**
     * 手动构建分页结果
     */
    public static <T> PageResultVO<T> build(List<T> list, long total, int page, int size) {
        int totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;
        return new PageResultVO<>(list, total, page, size, totalPages);
    }

}
