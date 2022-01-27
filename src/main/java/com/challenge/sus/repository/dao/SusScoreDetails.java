package com.challenge.sus.repository.dao;

import com.challenge.sus.repository.util.DataConverterForScores;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "sus_score_detail")
public class SusScoreDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "system_name", length = 30, nullable = false)
    private String systemName;

    @Column(name = "sus_score", length = 5, nullable = false)
    private double susScore;

    @Column(name = "sus_answer_points", length = 25, nullable = false)
    @Convert(converter = DataConverterForScores.class)
    private List<String> susAnswers;

    @Column(name = "createdDate", length = 50, nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updatedDate", length = 50, nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        updatedDate = createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }


}
