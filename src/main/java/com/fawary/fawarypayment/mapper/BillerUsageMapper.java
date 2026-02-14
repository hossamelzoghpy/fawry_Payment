package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.BillerUsageDTO;
import com.fawary.fawarypayment.entity.BillerUsage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillerUsageMapper {
    BillerUsageDTO toDto(BillerUsage entity);
    BillerUsage toEntity(BillerUsageDTO dto);
}
