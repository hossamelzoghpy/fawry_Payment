package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import com.fawary.fawarypayment.mapper.FactorConfigMapper;
import com.fawary.fawarypayment.repo.ScoringFactorConfigRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoringFactorConfigService {
    private final ScoringFactorConfigRepo scoringFactorConfigRepo;
    private final FactorConfigMapper mapper;
    public ScoringFactorConfigService(ScoringFactorConfigRepo scoringFactorConfigRepo, FactorConfigMapper mapper) {
        this.scoringFactorConfigRepo = scoringFactorConfigRepo;
        this.mapper = mapper;
    }
    public List<ScoringFactorConfigDto> getActiveFactorConfigs() {
       return mapper.toDtoList(scoringFactorConfigRepo.findByEnabledTrue());

    }
}
