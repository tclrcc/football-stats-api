package com.tony.footballStats.dto.season;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class SeasonDto {
    private Long id;
    private String startDate;
    private String endDate;
    private String currentMatchday;
    private String winner;
}
