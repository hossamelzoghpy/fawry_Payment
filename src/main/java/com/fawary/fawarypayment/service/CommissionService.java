package com.fawary.fawarypayment.service;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.entity.GatewayConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CommissionService {

    private static final int MONEY_SCALE = 2;

    public BigDecimal calculate(GatewayConfigDTO gateway, BigDecimal amount) {
        if (gateway == null) {
            throw new IllegalArgumentException("gateway must not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
//        if (amount.signum() < 0) {
//            throw new IllegalArgumentException("amount must be >= 0");
//        }

        BigDecimal fixed = nullProtect(gateway.getFixedFee());
        BigDecimal percentage = nullProtect(gateway.getPercentageFee());

        BigDecimal percentPart = amount
                .multiply(percentage)
                .divide(BigDecimal.valueOf(100), MONEY_SCALE + 4, RoundingMode.HALF_UP);
        BigDecimal total = fixed.add(percentPart);

        return total.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal nullProtect(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}

