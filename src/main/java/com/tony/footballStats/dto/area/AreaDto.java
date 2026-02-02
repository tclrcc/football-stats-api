package com.tony.footballStats.dto.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class AreaDto {
    private Long id;
    private String name;
    private String code;
    @JsonProperty("flag")
    private String flagUrl;
}
