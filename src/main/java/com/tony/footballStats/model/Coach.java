package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "coaches")
public class Coach {
    @Id
    private Long id; // On garde l'ID de l'API

    private String name;
    private String nationality;
    private LocalDate dateOfBirth;

    // On aplatit le "contract" ici
    private String contractStart;
    private String contractUntil;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
