package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.cruds.GatewayAvailability;
import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateways/available")
@AllArgsConstructor
public class GatewayAvailabilityController {

    private final GatewayAvailability gatewayAvailability;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody GatewayAvailabilityDTO dto) {
        gatewayAvailability.insertGatewayAvailability(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "update")
    public ResponseEntity<Void> update(@RequestBody GatewayAvailabilityDTO dto) {
        gatewayAvailability.updateGatewayAvailability(dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value = "delete/{gatewayId}")
    public ResponseEntity<Void> delete(@PathVariable String gatewayId) {
        gatewayAvailability.deleteGatewayAvailability(gatewayId);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "get/{gatewayId}")
    public ResponseEntity<GatewayAvailabilityDTO> get(@PathVariable String gatewayId) {
        return ResponseEntity.ok(gatewayAvailability.getGatewayAvailability(gatewayId));
    }
    @GetMapping(value = "getAll")
    public ResponseEntity<Iterable<GatewayAvailabilityDTO>> getAll() {
        return ResponseEntity.ok(gatewayAvailability.getAllGatewayAvailabilities());
    }
}
