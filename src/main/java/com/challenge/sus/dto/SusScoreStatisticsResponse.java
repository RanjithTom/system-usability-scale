package com.challenge.sus.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SusScoreStatisticsResponse {

    private PeriodType periodType;
    List<SusPeriodScores> periodScores;
}
