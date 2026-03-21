package com.jzsk.backendv2.utils;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;

import java.util.Collections;
import java.util.List;

public final class PageUtils {

    private static final long DEFAULT_PAGE = 1L;
    private static final long DEFAULT_SIZE = 10L;
    private static final long MAX_SIZE = 200L;

    private PageUtils() {
    }

    public static long normalizePage(Long page) {
        if (page == null || page < 1L) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    public static long normalizeSize(Long size) {
        if (size == null || size < 1L) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    public static BasePageQueryDTO normalize(BasePageQueryDTO queryDTO) {
        BasePageQueryDTO normalized = new BasePageQueryDTO();
        if (queryDTO == null) {
            normalized.setPage(DEFAULT_PAGE);
            normalized.setSize(DEFAULT_SIZE);
            return normalized;
        }
        normalized.setPage(normalizePage(queryDTO.getPage()));
        normalized.setSize(normalizeSize(queryDTO.getSize()));
        return normalized;
    }

    public static <T> PageResultVO<T> buildPage(List<T> list, long total, long page, long size) {
        List<T> safeList = list == null ? Collections.emptyList() : list;
        if (safeList.isEmpty() && total <= 0L) {
            return PageResultVO.empty(page, size);
        }
        return PageResultVO.of(safeList, total, page, size);
    }
}
