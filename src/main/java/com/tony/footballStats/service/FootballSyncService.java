package com.tony.footballStats.service;

import com.tony.footballStats.dto.team.TeamDto;
import com.tony.footballStats.dto.team.TeamListResponse;
import com.tony.footballStats.model.Coach;
import com.tony.footballStats.model.Player;
import com.tony.footballStats.model.Team;
import com.tony.footballStats.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FootballSyncService {

    @Value("${football.api.token}")
    private String apiToken;

    @Value("${football.api.base-url}")
    private String baseUrl;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 3600000)
    public void syncLigue1() {
        System.out.println("🔄 Début de la synchronisation Ligue 1...");
        String url = baseUrl + "/competitions/FL1/teams";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TeamListResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, TeamListResponse.class
            );

            if (response.getBody() != null && response.getBody().getTeams() != null) {
                List<TeamDto> teams = response.getBody().getTeams();
                System.out.println("📋 " + teams.size() + " équipes trouvées via DTO. Traitement...");

                for (TeamDto dto : teams) {
                    if (shouldUpdateTeam(dto.getId())) {
                        teamService.saveTeamFull(dto);
                    }
                }
            }
            System.out.println("✅ Synchronisation terminée avec succès.");
        } catch (Exception e) {
            System.err.println("❌ Erreur de sync : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean shouldUpdateTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .map(team -> team.getLastUpdated() == null ||
                        team.getLastUpdated().isBefore(LocalDateTime.now().minusDays(7)))
                .orElse(true);
    }
}
