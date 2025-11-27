package com.orioncode.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMetadata {
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
