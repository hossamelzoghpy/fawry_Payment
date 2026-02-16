package com.fawry.fawrypayment.mapper;

import com.fawry.fawrypayment.dto.TransactionDTO;
import com.fawry.fawrypayment.entity.TransactionLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "gateway.id", target = "gatewayId")
    TransactionDTO toDto(TransactionLog entity);

    @Mapping(target = "gateway.id", source = "gatewayId")
    TransactionLog toEntity(TransactionDTO dto);
    List<TransactionDTO> toDtoList(List<TransactionLog> entities);
}

