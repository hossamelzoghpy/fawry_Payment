package com.fawry.fawrypayment.mapper;

import com.fawry.fawrypayment.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {



    @Named("toRecommendedGateway")
    @Mapping(target = "id", source = "gateway.id")
    @Mapping(target = "name", source = "gateway.name")
    @Mapping(target = "estimatedCommission", source = "commission")
    @Mapping(target = "processingTime", expression = "java(formatProcessingTime(scored.getProcessingTime()))")
    SimpleGatewayResponse toRecommendedGateway(ScoredGatewayDto scored);

    @Named("toAlternativeGateway")
    @Mapping(target = "id", source = "gateway.id")
    @Mapping(target = "name", source = "gateway.name")
    @Mapping(target = "estimatedCommission", source = "commission")
    @Mapping(target = "processingTime", expression = "java(formatProcessingTime(scored.getProcessingTime()))")
    SimpleGatewayResponse toAlternativeGateway(ScoredGatewayDto scored);

    @Named("toAlternativeGatewayList")
    @IterableMapping(qualifiedByName = "toAlternativeGateway")
    List<SimpleGatewayResponse> toAlternativeGatewayList(List<ScoredGatewayDto> alternatives);


    default String formatProcessingTime(int minutes) {
        return minutes == 0 ? "Instant" : minutes + " min";
    }
}