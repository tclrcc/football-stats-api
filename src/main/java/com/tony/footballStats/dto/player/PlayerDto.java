package com.tony.footballStats.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDto {
    private Long id;
    private String name;
    private String position;
    private String nationality;
    private String dateOfBirth;
}
