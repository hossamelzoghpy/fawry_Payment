package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.cruds.GatewayConfig;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateways/config")
@AllArgsConstructor
public class GatewayConfigController {
    private final GatewayConfig gatewayConfig;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody GatewayConfigDTO dto) {
        gatewayConfig.insertGateway(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping(value = "update")
    public ResponseEntity<Void> update(@RequestBody GatewayConfigDTO dto) {
        gatewayConfig.updateGateway(dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value = "delete/{gatewayId}")
    public ResponseEntity<Void> delete(@PathVariable String gatewayId) {
        gatewayConfig.deleteGateway(gatewayId);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "get/{gatewayId}")
    public ResponseEntity<GatewayConfigDTO> get(@PathVariable String gatewayId) {
        return ResponseEntity.ok(gatewayConfig.getGateway(gatewayId));
    }
    @GetMapping(value = "getAll")
    public ResponseEntity<Iterable<GatewayConfigDTO>> getAll() {
        return ResponseEntity.ok(gatewayConfig.getAllGateways());
    }
}
