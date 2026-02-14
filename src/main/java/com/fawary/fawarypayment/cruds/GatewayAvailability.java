package com.fawary.fawarypayment.cruds;

import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import com.fawary.fawarypayment.exception.NotFountEx;
import com.fawary.fawarypayment.mapper.GatewayAvailabilityMapper;
import com.fawary.fawarypayment.repo.GatewayAvailabilityRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GatewayAvailability {
    private final GatewayAvailabilityRepo gatewayAvailabilityRepo;
    private final GatewayAvailabilityMapper mapper;
    public void insertGatewayAvailability(GatewayAvailabilityDTO dto){
        gatewayAvailabilityRepo.save(mapper.toEntity(dto));
    }
    public void updateGatewayAvailability(GatewayAvailabilityDTO dto){
        gatewayAvailabilityRepo.save(mapper.toEntity(dto));
    }
    public void deleteGatewayAvailability(String gatewayId){
        gatewayAvailabilityRepo.deleteById(gatewayId);
    }
    public GatewayAvailabilityDTO getGatewayAvailability(String gatewayId){
        return mapper.toDto(gatewayAvailabilityRepo.findById(gatewayId)
                .orElseThrow(()->new NotFountEx("No gateway availability found with id: "+gatewayId+".")));
    }
    public List<GatewayAvailabilityDTO> getAllGatewayAvailabilities(){
        return mapper.toDtoList(gatewayAvailabilityRepo.findAll());
    }
}
