package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import com.fawary.fawarypayment.entity.GatewayAvailability;
import com.fawary.fawarypayment.entity.GatewayConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GatewayAvailabilityMapper {

    @Mapping(source = "gateway.id", target = "gatewayId")
    GatewayAvailabilityDTO toDto(GatewayAvailability entity);
    List<GatewayAvailabilityDTO> toDtoList(List<GatewayAvailability> entities);

    @Mapping(target = "gateway", source = "gatewayId")
    GatewayAvailability toEntity(GatewayAvailabilityDTO dto);

    default GatewayConfig mapGatewayIdToGwConfig(String gatewayId) {
        if (gatewayId == null) {
            return null;
        }
        GatewayConfig config = new GatewayConfig();
        config.setId(gatewayId);
        return config;
    }
}

