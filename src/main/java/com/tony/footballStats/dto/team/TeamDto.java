package com.tony.footballStats.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tony.footballStats.dto.area.AreaDto;
import com.tony.footballStats.dto.coach.CoachDto;
import com.tony.footballStats.dto.competition.RunningCompetitionDto;
import com.tony.footballStats.dto.player.PlayerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class TeamDto {
    private AreaDto area;

    private Long id;
    private String name;
    private String shortName; // Nouveau
    private String tla;

    @JsonProperty("crest")
    private String crestUrl;

    private String address;
    private String website;   // Nouveau
    private Integer founded;
    private String clubColors; // Nouveau

    @JsonProperty("venue")
    private String stadium;

    private List<RunningCompetitionDto> runningCompetitions;

    // --- Les relations imbriquées ---
    private CoachDto coach;
    private List<PlayerDto> squad;
}
