package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.dto.RecommendRequestDTO;
import com.fawary.fawarypayment.dto.RecommendationResponse;
import com.fawary.fawarypayment.mapper.RecommendationMapper;
import com.fawary.fawarypayment.service.RoutingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentRecommendedController {

    private final RoutingService routingService;

    public PaymentRecommendedController(RoutingService routingService) {
        this.routingService = routingService;
    }

    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResponse> recommendGateway(@RequestBody @Validated RecommendRequestDTO requestDTO) {
        RecommendationResponse response = routingService.recommend(requestDTO);
        return ResponseEntity.ok(response);
    }
}
