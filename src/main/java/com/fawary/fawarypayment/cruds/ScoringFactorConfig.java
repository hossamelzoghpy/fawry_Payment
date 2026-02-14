package com.fawary.fawarypayment.cruds;

import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import com.fawary.fawarypayment.exception.NotFountEx;
import com.fawary.fawarypayment.mapper.FactorConfigMapper;
import com.fawary.fawarypayment.repo.ScoringFactorConfigRepo;
import com.fawary.fawarypayment.scoringengine.ScoringFactorConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScoringFactorConfig {
    private final ScoringFactorConfigRepo scoringFactorConfigRepo;
    private final FactorConfigMapper mapper;

    public void insertFactorConfig(ScoringFactoringConfig entity){
        scoringFactorConfigRepo.save(entity);
    }
    public void updateFactorConfig(ScoringFactoringConfig entity){
        scoringFactorConfigRepo.save(entity);
    }

    public void deleteFactorConfig(String code){
        scoringFactorConfigRepo.deleteById(code);
    }
    public ScoringFactoringConfig getFactorConfig(String code){
        return scoringFactorConfigRepo.findById(code).orElseThrow(()->new NotFountEx("No factor config found with code: "+code+"."));
    }
    public List<ScoringFactoringConfig> getAllFactorConfigs(){
        return scoringFactorConfigRepo.findAll();
    }

}
