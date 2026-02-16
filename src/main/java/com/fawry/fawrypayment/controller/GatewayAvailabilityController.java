package com.fawry.fawrypayment.controller;

import com.fawry.fawrypayment.service.GatewayAvailabilityService;
import com.fawry.fawrypayment.dto.GatewayAvailabilityDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateways/availability")
@AllArgsConstructor
public class GatewayAvailabilityController {

    private final GatewayAvailabilityService gatewayAvailabilityService;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody @Validated GatewayAvailabilityDTO dto) {
        gatewayAvailabilityService.insertGatewayAvailability(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "update")
    public ResponseEntity<Void> update(@RequestBody @Validated GatewayAvailabilityDTO dto) {
        gatewayAvailabilityService.updateGatewayAvailability(dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value = "delete/{availabilityId}")
    public ResponseEntity<Void> delete(@PathVariable Long availabilityId) {
        gatewayAvailabilityService.deleteGatewayAvailability(availabilityId);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "get/{availabilityId}")
    public ResponseEntity<GatewayAvailabilityDTO> get(@PathVariable Long availabilityId) {
        return ResponseEntity.ok(gatewayAvailabilityService.getGatewayAvailability(availabilityId));
    }
    @GetMapping(value = "getAll")
    public ResponseEntity<List<GatewayAvailabilityDTO>> getAll() {
        return ResponseEntity.ok(gatewayAvailabilityService.getAllGatewayAvailabilities());
    }

    @GetMapping(value = "getAll/{gatewayId}")
    public ResponseEntity<List<GatewayAvailabilityDTO>> getAllGatewayAvailabilitiesByGwId(@PathVariable String gatewayId) {
        return ResponseEntity.ok(gatewayAvailabilityService.getAllGatewayAvailabilitiesForGateway(gatewayId));
    }
}
