package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.exception.AlreadyExistsException;
import com.fawary.fawarypayment.exception.ApplicationException;
import com.fawary.fawarypayment.exception.NotFountException;
import com.fawary.fawarypayment.mapper.GatewayConfigMapper;
import com.fawary.fawarypayment.repo.GatewayConfigRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GatewayConfigService {
    private final GatewayConfigRepo gatewayConfigRepo;
    private final GatewayConfigMapper mapper;

    public void insertGateway(GatewayConfigDTO dto){

        if (dto.getId() != null) throw new ApplicationException("Gateway id must be null while inserting new gateway.", HttpStatus.BAD_REQUEST);
        gatewayConfigRepo.save(mapper.toEntity(dto));
    }
    public void updateGateway(GatewayConfigDTO dto){
        if(gatewayConfigRepo.findById(dto.getId()).isEmpty())
            throw new NotFountException("Gateway with id: "+dto.getId()+" not found.");
        gatewayConfigRepo.save(mapper.toEntity(dto));
    }
    public void deleteGateway(String gatewayId){
        if(gatewayConfigRepo.findById(gatewayId).isEmpty())
            throw new NotFountException("Gateway with id: "+gatewayId+" not found.");
        gatewayConfigRepo.deleteById(gatewayId);
    }
    public GatewayConfigDTO getGateway(String gatewayId){
        return mapper.toDto(gatewayConfigRepo.findById(gatewayId).orElseThrow(()->new NotFountException("No gateway found with id: "+gatewayId+".")));
    }
    public List<GatewayConfigDTO> getAllGateways(){
        return mapper.toDtoList(gatewayConfigRepo.findAll());
    }
}
