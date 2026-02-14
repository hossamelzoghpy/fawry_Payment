package com.fawary.fawarypayment.repo;

import com.fawary.fawarypayment.entity.BillerUsage;
import com.fawary.fawarypayment.entity.BillerUsageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BillerUsageRepo extends JpaRepository<BillerUsage, BillerUsageId> {

    Optional<BillerUsage> findByBillerIdAndGatewayIdAndUsageDate(
            String billerId,
            String gatewayId,
            LocalDate usageDate
    );
}
