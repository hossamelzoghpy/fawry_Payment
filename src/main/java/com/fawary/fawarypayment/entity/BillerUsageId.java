package com.fawary.fawarypayment.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BillerUsageId {
    private String billerId;
    private String gatewayId;
    private LocalDate usageDate;
}
