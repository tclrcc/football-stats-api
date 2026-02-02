package com.tony.footballStats.dto.coach;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoachDto {
    private Long id;
    private String name;
    private String nationality;
    private String dateOfBirth; // Format "yyyy-MM-dd"
    // Informations du contrat
    private CoachContractDto contract;
}
