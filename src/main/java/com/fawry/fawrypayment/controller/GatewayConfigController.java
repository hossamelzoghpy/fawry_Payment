package com.fawry.fawrypayment.controller;

import com.fawry.fawrypayment.service.GatewayConfigService;
import com.fawry.fawrypayment.dto.GatewayConfigDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateways/config")
@AllArgsConstructor
public class GatewayConfigController {
    private final GatewayConfigService gatewayConfigService;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody @Validated GatewayConfigDTO dto) {
        gatewayConfigService.insertGateway(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping(value = "update")
    public ResponseEntity<Void> update(@RequestBody @Validated GatewayConfigDTO dto) {
        gatewayConfigService.updateGateway(dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value = "delete/{gatewayId}")
    public ResponseEntity<Void> delete(@PathVariable String gatewayId) {
        gatewayConfigService.deleteGateway(gatewayId);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "get/{gatewayId}")
    public ResponseEntity<GatewayConfigDTO> get(@PathVariable String gatewayId) {
        return ResponseEntity.ok(gatewayConfigService.getGateway(gatewayId));
    }
    @GetMapping(value = "getAll")
    public ResponseEntity<List<GatewayConfigDTO>> getAll() {
        return ResponseEntity.ok(gatewayConfigService.getAllGateways());
    }
}
