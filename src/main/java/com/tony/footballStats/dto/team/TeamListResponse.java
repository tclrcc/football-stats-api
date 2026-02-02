package com.tony.footballStats.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.footballStats.dto.competition.CompetitionDto;
import com.tony.footballStats.dto.season.SeasonDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class TeamListResponse {
    private CompetitionDto competition;
    private SeasonDto season;
    private List<TeamDto> teams;
}
