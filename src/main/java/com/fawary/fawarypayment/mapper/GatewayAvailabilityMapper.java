package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import com.fawary.fawarypayment.entity.GatewayAvailability;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GatewayAvailabilityMapper {
    GatewayAvailabilityDTO toDto(GatewayAvailability entity);
    List<GatewayAvailabilityDTO> toDtoList(List<GatewayAvailability> entities);
    GatewayAvailability toEntity(GatewayAvailabilityDTO dto);
}

