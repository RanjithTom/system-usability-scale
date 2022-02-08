package com.challenge.sus.controller;

import com.challenge.sus.dto.PeriodType;
import com.challenge.sus.dto.SusScoreRequest;
import com.challenge.sus.dto.SusScoreResponse;
import com.challenge.sus.dto.SusScoreStatisticsResponse;
import com.challenge.sus.exception.BusinessException;
import com.challenge.sus.service.SusScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "sus")
public class SusScoreController {

    private final SusScoreService susScoreService;

    private static final Logger log = LoggerFactory.getLogger(SusScoreController.class);

    public SusScoreController(SusScoreService susScoreService) {
        this.susScoreService = susScoreService;
    }

    @PostMapping(value = "/compute", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<SusScoreResponse> calculateSusScore(@RequestBody @Valid SusScoreRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(susScoreService.calculateSusScore(request));
    }

    @GetMapping(value = "/statistics", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SusScoreStatisticsResponse> getSusStatistics(@RequestParam(required = false, name = "period") PeriodType period,
                                                                       @RequestParam(required = false, name = "startDate") LocalDate start,
                                                                       @RequestParam(required = false, name = "endDate") LocalDate end) {

        if(period == null) {
            period = PeriodType.ALL;
        }
        if(start == null) {
            start = LocalDate.now().minusMonths(6);
        }
        if(end == null) {
            end = LocalDate.now();
        }

        return ResponseEntity.status(HttpStatus.OK).body(susScoreService.getSusScoreStatistics(period, start, end));
    }
}
