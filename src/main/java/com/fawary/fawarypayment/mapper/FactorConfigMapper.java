package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FactorConfigMapper {
    ScoringFactorConfigDto toDto(ScoringFactoringConfig entity);
   List< ScoringFactorConfigDto> toDtoList(List<ScoringFactoringConfig> entities);
    ScoringFactoringConfig toEntity(ScoringFactorConfigDto dto);

}
