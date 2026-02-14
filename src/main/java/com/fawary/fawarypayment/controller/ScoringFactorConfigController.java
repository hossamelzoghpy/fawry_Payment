package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.cruds.ScoringFactorConfig;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factor/config")
@AllArgsConstructor
public class ScoringFactorConfigController {
    private final ScoringFactorConfig scoringFactorConfig;

    @PostMapping("/create")
    public ResponseEntity<Void> createFactorConfig(ScoringFactoringConfig entity){
        scoringFactorConfig.insertFactorConfig(entity);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update")
    public ResponseEntity<Void> updateFactorConfig(ScoringFactoringConfig entity){
        scoringFactorConfig.updateFactorConfig(entity);
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
