package com.fawary.fawarypayment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatewayAvailabilityDTO {
    private Long id;
    private String gatewayId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}

