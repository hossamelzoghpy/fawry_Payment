package com.fawry.fawrypayment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class PaginatedResponseDto <T>{
    private List<T> data;
    private int totalPages;
    private int currentPage;
    private int pageSize;

}
