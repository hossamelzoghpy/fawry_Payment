package com.fawry.fawrypayment.repo;

import com.fawry.fawrypayment.entity.GatewayAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface GatewayAvailabilityRepo extends JpaRepository<GatewayAvailability, Long> {


    List<GatewayAvailability> findByGatewayId(String gatewayId);

    List<GatewayAvailability> findByGatewayIdAndDayOfWeek(String gatewayId, DayOfWeek dayOfWeek);
}
