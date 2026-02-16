package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import com.fawary.fawarypayment.service.ScoringFactorConfigService;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factor/config")
@AllArgsConstructor
public class ScoringFactorConfigController {
    private final ScoringFactorConfigService scoringFactorConfig;

    @PostMapping("/create")
    public ResponseEntity<Void> createFactorConfig(@RequestBody  @Validated  ScoringFactorConfigDto scoringFactorConfigDto){
        scoringFactorConfig.insertFactorConfig(scoringFactorConfigDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/update")
    public ResponseEntity<Void> updateFactorConfig(@RequestBody @Validated ScoringFactorConfigDto scoringFactorConfigDto){
        scoringFactorConfig.updateFactorConfig(scoringFactorConfigDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping ("/delete")
    public ResponseEntity<Void> deleteFactorConfig(String code){
        scoringFactorConfig.deleteFactorConfig(code);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/get")
    public ResponseEntity<ScoringFactoringConfig> getFactorConfig(String code){
        return ResponseEntity.ok(scoringFactorConfig.getFactorConfig(code));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<ScoringFactoringConfig>> getAllFactorConfigs(){
        return ResponseEntity.ok(scoringFactorConfig.getAllFactorConfigs());
    }
}
