package com.fawry.fawrypayment.controller;

import com.fawry.fawrypayment.dto.RecommendRequestDTO;
import com.fawry.fawrypayment.dto.RecommendationResponse;
import com.fawry.fawrypayment.dto.RecommendationSplitResponse;
import com.fawry.fawrypayment.service.RoutingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentSplitingController {

    private final RoutingService routingService;


    public PaymentSplitingController(RoutingService routingService ) {
        this.routingService = routingService;

    }

    @PostMapping("/split")
    public ResponseEntity<RecommendationSplitResponse> recommendGateway(@RequestBody @Validated RecommendRequestDTO requestDTO) {
        RecommendationSplitResponse response = routingService.recommendWithSplitting(requestDTO);
        return ResponseEntity.ok(response);
    }
}
