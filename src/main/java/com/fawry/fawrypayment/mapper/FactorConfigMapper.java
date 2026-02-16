package com.fawry.fawrypayment.mapper;

import com.fawry.fawrypayment.dto.ScoringFactorConfigDto;
import com.fawry.fawrypayment.entity.ScoringFactoringConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FactorConfigMapper {
    ScoringFactorConfigDto toDto(ScoringFactoringConfig entity);
   List< ScoringFactorConfigDto> toDtoList(List<ScoringFactoringConfig> entities);
    ScoringFactoringConfig toEntity(ScoringFactorConfigDto dto);

}
