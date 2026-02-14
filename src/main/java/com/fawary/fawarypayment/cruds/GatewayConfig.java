package com.fawary.fawarypayment.cruds;

import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.exception.IllegalArgumentEx;
import com.fawary.fawarypayment.exception.NotFountEx;
import com.fawary.fawarypayment.mapper.GatewayConfigMapper;
import com.fawary.fawarypayment.repo.GatewayConfigRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GatewayConfig {
    private final GatewayConfigRepo gatewayConfigRepo;
    private final GatewayConfigMapper mapper;

    public void insertGateway(GatewayConfigDTO dto){

        gatewayConfigRepo.save(mapper.toEntity(dto));
    }
    public void updateGateway(GatewayConfigDTO dto){
        gatewayConfigRepo.save(mapper.toEntity(dto));
    }
    public void deleteGateway(String gatewayId){
        gatewayConfigRepo.deleteById(gatewayId);
    }
    public GatewayConfigDTO getGateway(String gatewayId){
        return mapper.toDto(gatewayConfigRepo.findById(gatewayId).orElseThrow(()->new NotFountEx("No gateway found with id: "+gatewayId+".")));
    }
    public List<GatewayConfigDTO> getAllGateways(){
        return mapper.toDtoList(gatewayConfigRepo.findAll());
    }
}
