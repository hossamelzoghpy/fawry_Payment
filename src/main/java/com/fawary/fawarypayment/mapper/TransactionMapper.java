package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.TransactionDTO;
import com.fawary.fawarypayment.entity.TransactionLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDto(TransactionLog entity);
    TransactionLog toEntity(TransactionDTO dto);
}

