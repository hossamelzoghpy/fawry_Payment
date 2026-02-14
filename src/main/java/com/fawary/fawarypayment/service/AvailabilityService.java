package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.dto.GatewayAvailabilityDTO;
import com.fawary.fawarypayment.mapper.GatewayAvailabilityMapper;
import com.fawary.fawarypayment.repo.GatewayAvailabilityRepo;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final GatewayAvailabilityRepo availabilityRepo;
    private final GatewayAvailabilityMapper mapper;

    public AvailabilityService(GatewayAvailabilityRepo availabilityRepo, GatewayAvailabilityMapper mapper) {
        this.availabilityRepo = availabilityRepo;
        this.mapper = mapper;
    }

    public boolean isAvailable(String gatewayId, LocalDateTime dateTime) {
        if (gatewayId == null || gatewayId.isBlank()) {
            throw new IllegalArgumentException("gatewayId must not be blank");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime must not be null");
        }

        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        LocalTime timeNow = dateTime.toLocalTime();

        List<GatewayAvailabilityDTO> getAvaible= mapper.toDtoList(availabilityRepo.findByGatewayIdAndDayOfWeek(gatewayId, dayOfWeek));

        if (getAvaible.isEmpty()) {
            return false;
        }

        return getAvaible.stream().anyMatch(ga ->
                !timeNow.isBefore(ga.getStartTime()) && timeNow.isBefore(ga.getEndTime())
        );
    }
    public boolean isAvailableNow(String gatewayId) {
        return isAvailable(gatewayId, LocalDateTime.now());
    }
}
