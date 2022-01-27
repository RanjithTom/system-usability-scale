package com.challenge.sus.repository;

import com.challenge.sus.repository.dao.SusScoreDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SusScoreRepo extends JpaRepository<SusScoreDetails, Long> {

    Optional<List<SusScoreDetails>> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

}
