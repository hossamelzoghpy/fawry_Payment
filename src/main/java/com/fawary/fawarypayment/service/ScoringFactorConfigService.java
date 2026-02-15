package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import com.fawary.fawarypayment.exception.NotFountException;
import com.fawary.fawarypayment.mapper.FactorConfigMapper;
import com.fawary.fawarypayment.repo.ScoringFactorConfigRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScoringFactorConfigService {
    private final ScoringFactorConfigRepo scoringFactorConfigRepo;
    private final FactorConfigMapper mapper;

    public void insertFactorConfig(ScoringFactorConfigDto scoringFactorConfigDto){
        if(scoringFactorConfigRepo.findById(scoringFactorConfigDto.code()).isPresent())
            throw new RuntimeException("Factor config with code: "+scoringFactorConfigDto.code()+" already exists.");
        scoringFactorConfigRepo.save(mapper.toEntity(scoringFactorConfigDto));
    }
    public void updateFactorConfig(ScoringFactorConfigDto scoringFactorConfigDto){
        if(scoringFactorConfigRepo.findById(scoringFactorConfigDto.code()).isPresent())
            throw new RuntimeException("Factor config with code: "+scoringFactorConfigDto.code()+" already exists.");
        scoringFactorConfigRepo.save(mapper.toEntity(scoringFactorConfigDto));
    }

    public void deleteFactorConfig(String code){
        if(scoringFactorConfigRepo.findById(code).isEmpty())
            throw new NotFountException("Factor config with code: "+code+" not found.");
        scoringFactorConfigRepo.deleteById(code);
    }
    public ScoringFactoringConfig getFactorConfig(String code){
        return scoringFactorConfigRepo.findById(code).orElseThrow(()->new NotFountException("No factor config found with code: "+code+"."));
    }

    public List<ScoringFactorConfigDto> getActiveFactorConfigs() {
        return mapper.toDtoList(scoringFactorConfigRepo.findByEnabledTrue());

    }
    public List<ScoringFactoringConfig> getAllFactorConfigs(){
        return scoringFactorConfigRepo.findAll();
    }

}
