package com.challenge.sus.controller;

import com.challenge.sus.dto.*;
import com.challenge.sus.exception.GlobalExceptionHandler;
import com.challenge.sus.service.SusScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SusScoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SusScoreService susScoreService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        SusScoreController susScoreController = new SusScoreController(susScoreService);
        mockMvc = MockMvcBuilders.standaloneSetup(susScoreController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }


    @Test
    void calculateSusScore() throws Exception {

        SusScoreRequest susScoreRequest = new SusScoreRequest();
        List<Integer> answerPoints = Arrays.asList(5,1,5,4,2,3,3,4,3,4);
        susScoreRequest.setAnswerPoints(answerPoints);
        SusScoreResponse susScoreResponse = SusScoreResponse.builder().usabilityScore(55.0).build();
        when(susScoreService.calculateSusScore(Mockito.isA(SusScoreRequest.class))).thenReturn(susScoreResponse);
        this.mockMvc
                .perform(
                        post("/sus/compute")
                                .content(objectMapper.writeValueAsBytes(susScoreRequest))
                                .header("Content-Type", "application/json"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usabilityScore", is(55.0)));
    }

    @Test
    void calculateSusScore_ShouldReturnBadRequest() throws Exception {

        SusScoreRequest susScoreRequest = new SusScoreRequest();
        List<Integer> answerPoints = Arrays.asList(5,1,5,4,2,3,3,4,3);
        susScoreRequest.setAnswerPoints(answerPoints);
        SusScoreResponse susScoreResponse = SusScoreResponse.builder().usabilityScore(55.0).build();
        this.mockMvc
                .perform(
                        post("/sus/compute")
                                .content(objectMapper.writeValueAsBytes(susScoreRequest))
                                .header("Content-Type", "application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Need answers for 10 questions and values between 1 and 5")));
    }

    @Test
    void getSusScoreStatistics_success() throws Exception {
        when(susScoreService.getSusScoreStatistics(PeriodType.ALL, LocalDate.now().minusMonths(6), LocalDate.now())).thenReturn(getSusStatisticsSuccess(PeriodType.ALL));
        this.mockMvc
                .perform(
                        get("/sus/statistics")
                                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.periodType", is("ALL")));
    }

    @Test
    void getSusScoreStatisticsError_OnInvalidType() throws Exception {
        this.mockMvc
                .perform(
                        get("/sus/statistics?period=RANDOM")
                                .header("Content-Type", "application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Not able to process the Request, Make sure the request is as per contract")));
    }

    private SusScoreStatisticsResponse getSusStatisticsSuccess(PeriodType periodType) {
        SusPeriodScores susPeriodScore = SusPeriodScores.builder().responseCount(1).susScore(55.0).build();
        List<SusPeriodScores> periodScores = List.of(susPeriodScore);
        return SusScoreStatisticsResponse.builder().periodScores(periodScores).periodType(periodType).build();
    }
}