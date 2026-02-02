package com.tony.footballStats.dto.coach;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoachContractDto {
    // Début et fin du contrat en cours
    private String start;
    private String until;
}
