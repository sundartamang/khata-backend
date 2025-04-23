package com.khata.utils;

import com.khata.payload.PaginationResponse;
import org.springframework.data.domain.Page;

public class PaginationUtil {
    public static <T> PaginationResponse<T> buildPaginationResponse(Page<T> page) {
        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}