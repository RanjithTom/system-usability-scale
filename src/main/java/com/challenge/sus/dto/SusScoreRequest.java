package com.challenge.sus.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SusScoreRequest {

    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans1;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans2;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans3;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans4;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans5;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans6;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans7;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans8;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Answer should be between 1 and 5")
    @Max(value = 5, message = "Answer should be between 1 and 5")
    private Integer ans9;
    @NotNull(message = "Answer is mandatory for all question")
    @Min(value = 1, message = "Value should be between 1 and 5")
    @Max(value = 5, message = "Value should be between 1 and 5")
    private Integer ans10;


}
