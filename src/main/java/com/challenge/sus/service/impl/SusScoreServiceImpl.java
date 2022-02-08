package com.challenge.sus.service.impl;

import com.challenge.sus.dto.*;
import com.challenge.sus.repository.SusScoreRepo;
import com.challenge.sus.repository.dao.SusScoreDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.challenge.sus.service.SusScoreService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SusScoreServiceImpl implements SusScoreService {

    private final SusScoreRepo susScoreRepo;

    private static final Logger log = LoggerFactory.getLogger(SusScoreServiceImpl.class);

    public SusScoreServiceImpl(SusScoreRepo susScoreRepo) {
        this.susScoreRepo = susScoreRepo;
    }

    @Override
    public SusScoreResponse calculateSusScore(SusScoreRequest request) {
        List<Integer> answerPoints = Arrays.asList(request.getAns1(), request.getAns2(),
                request.getAns3(), request.getAns4(),request.getAns5(), request.getAns6(),
                request.getAns7(), request.getAns8(),request.getAns9(), request.getAns10());
        double susScore = calculateSusScoreForRequest(answerPoints);

        SusScoreDetails scoreDetails = susScoreRepo.save(mapToSusScoreEntity(susScore, answerPoints));
        log.info("Calculated SUS score {}", susScore);
        return SusScoreResponse.builder().usabilityScore(scoreDetails.getSusScore()).build();
    }

    @Override
    public SusScoreStatisticsResponse getSusScoreStatistics(PeriodType periodType, LocalDate startDate, LocalDate endDate) {
        Optional<List<SusScoreDetails>> susScoreDetails = susScoreRepo.findByCreatedDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        return susScoreDetails.map(susScoreList -> createSusScoreStatistics(periodType, susScoreList))
                .orElse(new SusScoreStatisticsResponse());
    }

    private static SusScoreStatisticsResponse createSusScoreStatistics(PeriodType periodType, List<SusScoreDetails> susScoreDetails) {
        SusScoreStatisticsResponse susScoreStatisticsResponse = new SusScoreStatisticsResponse();

        List<SusPeriodScores> periodScores;
        if(periodType == PeriodType.ALL){
            SusPeriodScores score = new SusPeriodScores();
            score.setSusScore(getSusScoreForPeriod(susScoreDetails));
            score.setResponseCount(susScoreDetails.size());
            periodScores = Arrays.asList(score);
        } else {
            Map<LocalDateTime, List<SusScoreDetails>> groupedResults = getSusScoreGroupedResults(periodType, susScoreDetails);
            periodScores = getAverageSusScoreResults(groupedResults);
        }
        log.info("Returned Statistics response for period type {} and total count of score {}", periodType, periodScores.size());
        susScoreStatisticsResponse.setPeriodType(periodType);
        susScoreStatisticsResponse.setPeriodScores(periodScores);
        return susScoreStatisticsResponse;
    }

    private static List<SusPeriodScores> getAverageSusScoreResults(Map<LocalDateTime, List<SusScoreDetails>> groupedResults) {
        List<SusPeriodScores> periodScores = new ArrayList<>();
        SusPeriodScores score;
        double susScoreForPeriod = 0;
        for (var entry : groupedResults.entrySet()) {
            score = new SusPeriodScores();
            score.setPeriod(entry.getKey());
            score.setResponseCount(entry.getValue().size());
            susScoreForPeriod = getSusScoreForPeriod(entry.getValue());
            score.setSusScore(susScoreForPeriod);
            periodScores.add(score);
        }
        return periodScores;
    }

    private static double getSusScoreForPeriod(List<SusScoreDetails> entry) {
        double susScoreForPeriod;
        susScoreForPeriod = entry.stream().mapToDouble(SusScoreDetails::getSusScore).sum();
        susScoreForPeriod = susScoreForPeriod / entry.size();
        return susScoreForPeriod;
    }

    private static Map<LocalDateTime, List<SusScoreDetails>> getSusScoreGroupedResults(PeriodType periodType, List<SusScoreDetails> susDetails) {
        Map<LocalDate, List<SusScoreDetails>> groupedResultsWithDate;
        Map<LocalDateTime, List<SusScoreDetails>> groupedResults = new HashMap<>();

        if (periodType == PeriodType.HOUR) {
            groupedResults = susDetails.stream().collect(groupingBy(d -> d.getCreatedDate().truncatedTo(ChronoUnit.HOURS)));
        } else {
            switch (periodType) {
                case DAY: {
                    groupedResultsWithDate = susDetails.stream().collect(groupingBy(d -> d.getCreatedDate().toLocalDate()));
                    break;
                }
                case WEEK: {
                    groupedResultsWithDate = susDetails.stream()
                            .collect(groupingBy(d -> d.getCreatedDate().toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))));
                    break;
                }
                case MONTH: {
                    groupedResultsWithDate = susDetails.stream()
                            .collect(groupingBy(d -> d.getCreatedDate().toLocalDate().withDayOfMonth(1)));
                    break;
                }
                case YEAR: {
                    groupedResultsWithDate = susDetails.stream()
                            .collect(groupingBy(d -> d.getCreatedDate().toLocalDate().withMonth(1).withDayOfMonth(1)));
                    break;
                }
                default:
                    groupedResultsWithDate = new HashMap<>();
            }
            for (var entry : groupedResultsWithDate.entrySet()) {
                groupedResults.put(entry.getKey().atStartOfDay(), entry.getValue());
            }
        }
        return groupedResults;
    }


    /*
    public static void main(String args[]) {
        List<SusScoreDetails> susScoreDetails = new ArrayList<>();
        SusScoreDetails sus = new SusScoreDetails();
        sus.setSusScore(4);
        sus.setCreatedDate(LocalDateTime.now());
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(80.9);
        sus.setCreatedDate(LocalDateTime.now());
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(17.9);
        sus.setCreatedDate(LocalDateTime.now().minusDays(1));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(17.9);
        sus.setCreatedDate(LocalDateTime.now().minusHours(2));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(10.9);
        sus.setCreatedDate(LocalDateTime.now().minusHours(2).minusMinutes(6));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(17.9);
        sus.setCreatedDate(LocalDateTime.now().minusDays(3));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(10.9);
        sus.setCreatedDate(LocalDateTime.now().minusDays(5));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(10.9);
        sus.setCreatedDate(LocalDateTime.now().minusDays(5));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(20.9);
        sus.setCreatedDate(LocalDateTime.now().minusMonths(1));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(20.9);
        sus.setCreatedDate(LocalDateTime.now().minusMonths(1));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(10.9);
        sus.setCreatedDate(LocalDateTime.now().minusMonths(2));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(10.9);
        sus.setCreatedDate(LocalDateTime.now().minusMonths(3));
        susScoreDetails.add(sus);

        sus = new SusScoreDetails();
        sus.setSusScore(10.9);
        sus.setCreatedDate(LocalDateTime.now().minusDays(5));
        susScoreDetails.add(sus);

        susScoreDetails.forEach(susScoreDetails1 -> System.out.println(susScoreDetails1.getCreatedDate()+"  "+susScoreDetails1.getSusScore()));
        createSusScoreStatistics("HOUR", Optional.of(susScoreDetails));

    }*/


    private SusScoreDetails mapToSusScoreEntity(double susScore, List<Integer> answers) {
        List<String> answerPoints = answers.stream().map(Object::toString)
                .collect(Collectors.toList());
        return SusScoreDetails.builder().susScore(susScore).susAnswers(answerPoints).systemName("my system").build();
    }

    private double calculateSusScoreForRequest(List<Integer> answerPoints) {
        double susScore = 0;
        for (int i = 0; i < answerPoints.size(); i++) {
            if (i % 2 == 0)
                susScore += answerPoints.get(i) - 1;
            else
                susScore += 5 - answerPoints.get(i);
        }
        susScore *= 2.5;
        return susScore;
    }
}

