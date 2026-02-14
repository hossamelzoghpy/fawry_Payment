package com.fawary.fawarypayment.repo;

import com.fawary.fawarypayment.entity.GatewayAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayAvailabilityRepo extends JpaRepository<GatewayAvailability, String> {


    List<GatewayAvailability> findByGatewayId(String gatewayId);

    List<GatewayAvailability> findByGatewayIdAndDayOfWeek(String gatewayId, Integer dayOfWeek);
}
