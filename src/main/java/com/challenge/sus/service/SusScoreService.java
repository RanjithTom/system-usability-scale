package com.challenge.sus.service;

import com.challenge.sus.dto.PeriodType;
import com.challenge.sus.dto.SusScoreRequest;
import com.challenge.sus.dto.SusScoreResponse;
import com.challenge.sus.dto.SusScoreStatisticsResponse;

import java.time.LocalDate;

public interface SusScoreService {

    SusScoreResponse calculateSusScore(SusScoreRequest request);

    SusScoreStatisticsResponse getSusScoreStatistics(PeriodType periodType, LocalDate startDate, LocalDate endDate);
}
