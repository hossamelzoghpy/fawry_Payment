package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.service.GatewayAvailabilityService;
import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateways/availability")
@AllArgsConstructor
public class GatewayAvailabilityController {

    private final GatewayAvailabilityService gatewayAvailabilityService;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody GatewayAvailabilityDTO dto) {
        gatewayAvailabilityService.insertGatewayAvailability(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "update")
    public ResponseEntity<Void> update(@RequestBody GatewayAvailabilityDTO dto) {
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
    public ResponseEntity<Iterable<GatewayAvailabilityDTO>> getAll() {
        return ResponseEntity.ok(gatewayAvailabilityService.getAllGatewayAvailabilities());
    }
}
