package com.tony.footballStats.service;

import com.tony.footballStats.dto.team.TeamDto;
import com.tony.footballStats.dto.team.TeamListResponse;
import com.tony.footballStats.model.League;
import com.tony.footballStats.repository.LeagueRepository;
import com.tony.footballStats.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FootballSyncService {

    @Value("${football.api.token}")
    private String apiToken;

    @Value("${football.api.base-url}")
    private String baseUrl;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 3600000)
    public void syncAllLeagues() {
        System.out.println("🌍 Début de la synchronisation globale...");

        // 1. On récupère seulement les ligues actives en BDD
        List<League> activeLeagues = leagueRepository.findByActiveTrue();

        for (League league : activeLeagues) {
            System.out.println("🔄 Traitement du championnat : " + league.getName() + " (" + league.getCode() + ")");
            syncLeague(league);

            // Petite pause entre chaque championnat pour être gentil avec l'API
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
        }

        System.out.println("✅ Synchronisation globale terminée.");
    }

    private void syncLeague(League league) {
        String url = baseUrl + "/competitions/" + league.getCode() + "/teams";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TeamListResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, TeamListResponse.class
            );

            if (response.getBody() != null && response.getBody().getTeams() != null) {
                List<TeamDto> teams = response.getBody().getTeams();
                System.out.println("   📋 " + teams.size() + " équipes trouvées pour " + league.getCode());

                for (TeamDto dto : teams) {
                    if (shouldUpdateTeam(dto.getId())) {
                        // On passe la ligue actuelle pour lier l'équipe
                        teamService.saveTeamFull(dto, league);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur sync " + league.getName() + " : " + e.getMessage());
        }
    }

    private boolean shouldUpdateTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .map(team -> team.getLastUpdated() == null ||
                        team.getLastUpdated().isBefore(LocalDateTime.now().minusDays(7)))
                .orElse(true);
    }
}
