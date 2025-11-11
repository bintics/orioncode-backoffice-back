package com.orioncode.backoffice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> data;
    private PaginationMetadata pagination;
    private SearchMetadata metadata;

    // Constructor sin metadata para compatibilidad
    public PageResponse(List<T> data, PaginationMetadata pagination) {
        this.data = data;
        this.pagination = pagination;
        this.metadata = null;
    }
}
