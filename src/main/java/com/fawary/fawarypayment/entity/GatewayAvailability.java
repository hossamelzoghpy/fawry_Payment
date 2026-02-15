package com.fawary.fawarypayment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "gateway_availability",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_gateway_day",
                        columnNames = {"gateway_id", "day_of_week"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gateway_id", nullable = false)
    private GatewayConfig gateway;

    @Column(name = "day_of_week", nullable = false)
    @NotNull
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
