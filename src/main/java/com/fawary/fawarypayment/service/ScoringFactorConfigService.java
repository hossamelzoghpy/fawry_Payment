package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import com.fawary.fawarypayment.exception.AlreadyExistsException;
import com.fawary.fawarypayment.exception.ApplicationException;
import com.fawary.fawarypayment.exception.NotFountException;
import com.fawary.fawarypayment.mapper.FactorConfigMapper;
import com.fawary.fawarypayment.repo.ScoringFactorConfigRepo;
import com.fawary.fawarypayment.service.scoringengine.FactorRegistry;
import com.fawary.fawarypayment.service.scoringengine.ScoringFactor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ScoringFactorConfigService {
    private final ScoringFactorConfigRepo scoringFactorConfigRepo;
    private final FactorConfigMapper mapper;
    private final FactorRegistry factorRegistry;

    @Transactional
    public void insertFactorConfig(ScoringFactorConfigDto scoringFactorConfigDto){
        if(scoringFactorConfigRepo.findById(scoringFactorConfigDto.getCode()).isPresent())
            throw new AlreadyExistsException("Factor config with code: "+scoringFactorConfigDto.getCode()+" already exists.");
        validateFactorConfigExistence(scoringFactorConfigDto);
        scoringFactorConfigRepo.save(mapper.toEntity(scoringFactorConfigDto));
    }
    public void updateFactorConfig(ScoringFactorConfigDto scoringFactorConfigDto){
        if(scoringFactorConfigRepo.findById(scoringFactorConfigDto.getCode()).isPresent())
            throw new AlreadyExistsException("Factor config with code: "+scoringFactorConfigDto.getCode()+" already exists.");
        scoringFactorConfigRepo.save(mapper.toEntity(scoringFactorConfigDto));
    }

    private void validateFactorConfigExistence(ScoringFactorConfigDto scoringFactorConfigDto){
        if (scoringFactorConfigDto.getCode() == null || scoringFactorConfigDto.getCode().isBlank())
            throw new IllegalArgumentException("code must not be null or blank");
        factorRegistry.getFactor(scoringFactorConfigDto.getCode()).orElseThrow(
                () -> new ApplicationException("No factor behaviour found with code: "+scoringFactorConfigDto.getCode(), HttpStatus.BAD_REQUEST));
    }
    public void deleteFactorConfig(String code){
        if(scoringFactorConfigRepo.findById(code).isEmpty())
            throw new NotFountException("Factor config with code: "+code+" not found.");
        scoringFactorConfigRepo.deleteById(code);
    }
    public ScoringFactoringConfig getFactorConfig(String code){
        return scoringFactorConfigRepo.findByCode(code).orElseThrow(()->new NotFountException("No factor config found with code: "+code+"."));
    }

    public List<ScoringFactorConfigDto> getActiveFactorConfigs() {
        return mapper.toDtoList(scoringFactorConfigRepo.findByEnabledTrue());

    }
    public List<ScoringFactoringConfig> getAllFactorConfigs(){
        return scoringFactorConfigRepo.findAll();
    }

}
