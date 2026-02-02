package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "teams")
// Important pour éviter les boucles infinies avec Lombok et les relations bidirectionnelles
@EqualsAndHashCode(exclude = {"coach", "players"})
@ToString(exclude = {"coach", "players"})
public class Team {
    @Id
    private Long id;

    private String name;
    private String shortName; // Ajouté depuis le DTO
    private String tla;
    private String crestUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String website;   // Ajouté depuis le DTO
    private Integer foundedYear;
    private String clubColors; // Ajouté depuis le DTO
    private String stadium;

    // Champs pour stocker les infos de l'objet "Area" du DTO simplement
    private String areaName;
    private String areaFlagUrl;

    private LocalDateTime lastUpdated;

    // --- RELATIONS ---

    // 1 Equipe a 1 Coach. Si on supprime l'équipe, on supprime le coach (orphanRemoval)
    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Coach coach;

    // 1 Equipe a N Joueurs.
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
}
