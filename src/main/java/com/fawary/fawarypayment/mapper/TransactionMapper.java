package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.dto.TransactionDTO;
import com.fawary.fawarypayment.entity.GatewayConfig;
import com.fawary.fawarypayment.entity.TransactionLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDto(TransactionLog entity);
    TransactionLog toEntity(TransactionDTO dto);
    List<TransactionDTO> toDtoList(List<TransactionLog> entities);
}

