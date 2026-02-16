package com.fawry.fawrypayment.dto;

import java.util.List;

public record RecommendationResponse (
    SimpleGatewayResponse recommendedGateway,
    List<SimpleGatewayResponse> alternatives)
{
}
