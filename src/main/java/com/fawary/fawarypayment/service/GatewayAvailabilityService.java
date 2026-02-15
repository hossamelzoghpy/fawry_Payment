package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import com.fawary.fawarypayment.exception.NotFountException;
import com.fawary.fawarypayment.mapper.GatewayAvailabilityMapper;
import com.fawary.fawarypayment.repo.GatewayAvailabilityRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GatewayAvailabilityService {
    private final GatewayAvailabilityRepo gatewayAvailabilityRepo;
    private final GatewayAvailabilityMapper mapper;
    private final GatewayConfigService gatewayConfigService;


    public void insertGatewayAvailability(GatewayAvailabilityDTO dto){
        if (gatewayConfigService.getGateway(dto.getGatewayId()) == null){
            throw new NotFountException("No gateway found with id: "+dto.getGatewayId()+".");
        }
        gatewayAvailabilityRepo.save(mapper.toEntity(dto));
    }
    public void updateGatewayAvailability(GatewayAvailabilityDTO dto){
        if (gatewayConfigService.getGateway(dto.getGatewayId()) == null){
            throw new NotFountException("No gateway found with id: "+dto.getGatewayId()+".");
        }
        if (gatewayAvailabilityRepo.findById(dto.getId()).isEmpty()){
            throw new NotFountException("No gateway availability found with id: "+dto.getId()+".");
        }
        gatewayAvailabilityRepo.save(mapper.toEntity(dto));
    }
    public void deleteGatewayAvailability(Long availabilityId){
        gatewayAvailabilityRepo.deleteById(availabilityId);
    }

    public GatewayAvailabilityDTO getGatewayAvailability(Long availabilityId){
        return mapper.toDto(gatewayAvailabilityRepo.findById(availabilityId)
                .orElseThrow(()->new NotFountException("No gateway availability found with id: "+availabilityId+".")));
    }
    public List<GatewayAvailabilityDTO> getAllGatewayAvailabilities(){
        return mapper.toDtoList(gatewayAvailabilityRepo.findAll());
    }
}
