package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.entity.GatewayConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GatewayConfigMapper {
    GatewayConfigDTO toDto(GatewayConfig entity);
    List<GatewayConfigDTO> toDtoList(List<GatewayConfig> entities);
    GatewayConfig toEntity(GatewayConfigDTO dto);
}

