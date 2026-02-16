package com.fawry.fawrypayment.mapper;

import com.fawry.fawrypayment.dto.GatewayConfigDTO;
import com.fawry.fawrypayment.entity.GatewayConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GatewayConfigMapper {
    GatewayConfigDTO toDto(GatewayConfig entity);
    List<GatewayConfigDTO> toDtoList(List<GatewayConfig> entities);
    GatewayConfig toEntity(GatewayConfigDTO dto);
}

