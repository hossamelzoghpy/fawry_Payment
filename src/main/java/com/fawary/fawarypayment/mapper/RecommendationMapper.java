package com.fawary.fawarypayment.mapper;

import com.fawary.fawarypayment.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {


    // ---------- Recommended mapping ----------
    @Named("toRecommendedGateway")
    @Mapping(target = "id", source = "gateway.id")
    @Mapping(target = "name", source = "gateway.name")
    @Mapping(target = "estimatedCommission", source = "commission")
    @Mapping(target = "processingTime", expression = "java(formatProcessingTime(scored.getProcessingTime()))")
    SimpleGatewayResponse toRecommendedGateway(ScoredGatewayDto scored);

    // ---------- Alternatives mapping (different rules) ----------
    @Named("toAlternativeGateway")
    @Mapping(target = "id", source = "gateway.id")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "estimatedCommission", source = "commission")
    @Mapping(target = "processingTime", ignore = true)
    SimpleGatewayResponse toAlternativeGateway(ScoredGatewayDto scored);

    @Named("toAlternativeGatewayList")
    @IterableMapping(qualifiedByName = "toAlternativeGateway")
    List<SimpleGatewayResponse> toAlternativeGatewayList(List<ScoredGatewayDto> alternatives);

    // Helper for formatting processing time
    default String formatProcessingTime(int minutes) {
        return minutes == 0 ? "Instant" : minutes + " min";
    }
}