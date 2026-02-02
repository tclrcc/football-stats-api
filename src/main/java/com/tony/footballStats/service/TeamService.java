package com.tony.footballStats.service;

import com.tony.footballStats.dto.team.TeamDto;
import com.tony.footballStats.model.Coach;
import com.tony.footballStats.model.Player;
import com.tony.footballStats.model.Team;
import com.tony.footballStats.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Transactional
    public void saveTeamFull(TeamDto dto) {
        // 1. Récupération ou création de l'équipe
        Team team = teamRepository.findById(dto.getId()).orElse(new Team());

        // 2. Mapping des données de base
        team.setId(dto.getId());
        team.setName(dto.getName());
        team.setShortName(dto.getShortName());
        team.setTla(dto.getTla());
        team.setCrestUrl(dto.getCrestUrl());
        team.setAddress(dto.getAddress());
        team.setWebsite(dto.getWebsite());
        team.setFoundedYear(dto.getFounded());
        team.setClubColors(dto.getClubColors());
        team.setStadium(dto.getStadium());
        team.setLastUpdated(LocalDateTime.now());

        // Mapping Area
        if (dto.getArea() != null) {
            team.setAreaName(dto.getArea().getName());
            team.setAreaFlagUrl(dto.getArea().getFlagUrl());
        }

        // 3. Mapping du Coach
        if (dto.getCoach() != null && dto.getCoach().getId() != null) {
            Coach coachEntity = new Coach();
            coachEntity.setId(dto.getCoach().getId());
            coachEntity.setName(dto.getCoach().getName());
            coachEntity.setNationality(dto.getCoach().getNationality());
            coachEntity.setDateOfBirth(parseDate(dto.getCoach().getDateOfBirth()));

            if (dto.getCoach().getContract() != null) {
                coachEntity.setContractStart(dto.getCoach().getContract().getStart());
                coachEntity.setContractUntil(dto.getCoach().getContract().getUntil());
            }

            coachEntity.setTeam(team);
            team.setCoach(coachEntity);
        }

        // 4. Mapping de l'effectif
        if (dto.getSquad() != null) {
            List<Player> newPlayers = dto.getSquad().stream()
                    .map(pDto -> {
                        Player p = new Player();
                        p.setId(pDto.getId());
                        p.setName(pDto.getName());
                        p.setPosition(pDto.getPosition());
                        p.setNationality(pDto.getNationality());
                        p.setDateOfBirth(parseDate(pDto.getDateOfBirth()));
                        p.setTeam(team);
                        return p;
                    })
                    .collect(Collectors.toList());

            if (team.getPlayers() == null) {
                team.setPlayers(newPlayers);
            } else {
                team.getPlayers().clear();
                team.getPlayers().addAll(newPlayers);
            }
        }

        teamRepository.save(team);
        System.out.println("💾 Équipe sauvegardée : " + team.getName());
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
