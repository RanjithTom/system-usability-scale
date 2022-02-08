package com.challenge.sus.service.impl;

import com.challenge.sus.dto.PeriodType;
import com.challenge.sus.dto.SusScoreRequest;
import com.challenge.sus.dto.SusScoreResponse;
import com.challenge.sus.dto.SusScoreStatisticsResponse;
import com.challenge.sus.repository.SusScoreRepo;
import com.challenge.sus.repository.dao.SusScoreDetails;
import com.challenge.sus.service.SusScoreService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SusScoreServiceImplTest {

    @Mock
    private SusScoreRepo susScoreRepo;

    private SusScoreService susScoreService;

    @BeforeEach
    public void setUp() {
        this.susScoreService = new SusScoreServiceImpl(susScoreRepo);
    }

    @Test
    void calculateSusScore_returnSuccess() {
        SusScoreRequest susScoreRequest = getSusScoreRequest();
        List<Integer> answerPoints = Arrays.asList(5, 1, 5, 4, 2, 3, 3, 4, 3, 4);

        when(susScoreRepo.save(Mockito.isA(SusScoreDetails.class))).thenReturn(getSusScore());
        SusScoreResponse response = susScoreService.calculateSusScore(susScoreRequest);
        verify(susScoreRepo, times(1)).save(Mockito.isA(SusScoreDetails.class));
        Assert.assertEquals(Double.valueOf(55), response.getUsabilityScore());
    }

    private SusScoreRequest getSusScoreRequest() {
        return SusScoreRequest.builder().ans1(5).ans2(1).ans3(5).ans4(4)
                .ans5(2).ans6(3).ans7(3).ans8(4).ans9(3).ans10(4).build();

    }

    @Test
    void getSusScoreStatistics_returnSuccess() {
        when(susScoreRepo.findByCreatedDateBetween(Mockito.isA(LocalDateTime.class), Mockito.isA(LocalDateTime.class))).thenReturn(getSusScoreDetails());
        SusScoreStatisticsResponse susResponse = susScoreService.getSusScoreStatistics(PeriodType.ALL, LocalDate.now(), LocalDate.now());
        verify(susScoreRepo, times(1)).findByCreatedDateBetween(Mockito.isA(LocalDateTime.class), Mockito.isA(LocalDateTime.class));
        Assert.assertEquals(PeriodType.ALL, susResponse.getPeriodType());
    }

    private Optional<List<SusScoreDetails>> getSusScoreDetails() {
        return Optional.of(List.of(SusScoreDetails.builder().susScore(55.0).build()));
    }

    private SusScoreDetails getSusScore() {
        return SusScoreDetails.builder().susScore(55.0).build();
    }
}