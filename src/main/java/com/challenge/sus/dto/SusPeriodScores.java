package com.challenge.sus.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SusPeriodScores {

    private LocalDateTime period;
    private Integer responseCount;
    private Double susScore;
}
