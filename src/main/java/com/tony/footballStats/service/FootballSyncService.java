package com.tony.footballStats.service;

import com.tony.footballStats.dto.team.TeamDto;
import com.tony.footballStats.dto.team.TeamListResponse;
import com.tony.footballStats.model.League;
import com.tony.footballStats.repository.LeagueRepository;
import com.tony.footballStats.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FootballSyncService {
    // Constantes connexions API
    @Value("${football.api.token}")
    private String apiToken;
    @Value("${football.api.base-url}")
    private String baseUrl;

    // Repositories
    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    // Services
    private final TeamService teamService;
    // Utils
    private final RestTemplate restTemplate;

    /**
     * Méthode import des données des leagues (API externe)
     */
    @Scheduled(fixedRate = 3600000)
    public void syncAllLeagues() {
        System.out.println("🌍 Vérification des mises à jour planifiées...");

        // Récupération des ligues actives (synchronisation = OUI)
        List<League> activeLeagues = leagueRepository.findByActiveTrue();

        for (League league : activeLeagues) {
            // Si la ligue a été mise à jour il y a moins de 24 heures, on passe
            if (isLeagueUpToDate(league)) {
                System.out.println("✅ " + league.getName() + " est déjà à jour (Dernière maj : " + league.getLastUpdated() + "). On ignore.");
                continue;
            }
            // ------------------------

            System.out.println("🔄 Lancement de la synchro pour : " + league.getName());
            syncLeague(league);

            try { Thread.sleep(5000); } catch (InterruptedException e) {}
        }

        System.out.println("🏁 Fin du cycle de vérification.");
    }

    /**
     * Renvoie true → import à faire, false sinon
     * @param league league à importer
     * @return true or false
     */
    private boolean isLeagueUpToDate(League league) {
        if (league.getLastUpdated() == null) return false; // Jamais mis à jour → on y va

        // On ne recharge pas la league si le dernier import < 24h
        return league.getLastUpdated().isAfter(LocalDateTime.now().minusDays(1));
    }

    /**
     * Appel API + traitement import
     * @param league league
     */
    private void syncLeague(League league) {
        // Construction de la requête
        String url = baseUrl + "/competitions/" + league.getCode() + "/teams";
        // Ajout des headers (TOKEN API)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Appel API
            ResponseEntity<TeamListResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, TeamListResponse.class
            );

            if (response.getBody() != null && response.getBody().getTeams() != null) {
                // Récupération des équipes JSON
                List<TeamDto> teams = response.getBody().getTeams();
                System.out.println("   📋 " + teams.size() + " équipes récupérées.");

                for (TeamDto dto : teams) {
                    if (shouldUpdateTeam(dto.getId())) {
                        // Création équipe
                        teamService.saveTeamFull(dto, league);
                    }
                }

                // On met à jour la date de la ligue ---
                league.setLastUpdated(LocalDateTime.now());
                leagueRepository.save(league);
                System.out.println("   📆 Date de mise à jour de la ligue actualisée.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur sync " + league.getName() + " : " + e.getMessage());
        }
    }

    /**
     * Renvoie true → mise à jour équipe, false sinon
     * @param teamId identifiant équipe
     * @return true or false
     */
    private boolean shouldUpdateTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .map(team -> team.getLastUpdated() == null ||
                        team.getLastUpdated().isBefore(LocalDateTime.now().minusDays(7)))
                .orElse(true);
    }
}
