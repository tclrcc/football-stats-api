package com.tony.footballStats.dto.competition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class RunningCompetitionDto {
    private Long id;
    private String name;
    private String code;
    private String type;
    @JsonProperty("emblem")
    private String emblemUrl;
}
