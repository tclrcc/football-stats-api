package com.tony.footballStats.service;

import com.tony.footballStats.model.Player;
import com.tony.footballStats.model.PlayerStats;
import com.tony.footballStats.model.Team;
import com.tony.footballStats.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class PlayerStatsService {
    private final PlayerRepository playerRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api-football.key}")
    private String apiKey;

    @Value("${api-football.host}")
    private String apiHost;

    @Value("${api-football.base-url}")
    private String apiUrl;

    @Value("${api-football.season}")
    private Integer season;

    @Transactional
    protected void fetchAndSaveStats(Team team) {
        try {
            // 1. Trouver l'ID de l'équipe chez API-Football
            Long externalTeamId = searchTeamId(team.getName());
            if (externalTeamId == null) return;

            // 2. Récupérer les stats des joueurs
            String url = apiUrl + "/players?team=" + externalTeamId + "&season=" + season;
            JsonNode root = callApi(url);

            if (root != null && root.path("response").isArray()) {
                for (JsonNode item : root.path("response")) {
                    String lastName = item.path("player").path("lastname").asText();
                    JsonNode statsNode = item.path("statistics").get(0);

                    // 3. Faire correspondre avec nos joueurs
                    matchPlayerAndSave(team, lastName, statsNode);
                }
                System.out.println("✅ Stats sauvegardées pour " + team.getName());
            }

        } catch (Exception e) {
            System.err.println("Erreur enrichissement : " + e.getMessage());
        }
    }



    public void matchPlayerAndSave(Team team, String lastName, JsonNode statsNode) {
        // Recherche approximative par nom de famille
        for (Player p : team.getPlayers()) {
            if (p.getName().toLowerCase().contains(lastName.toLowerCase())) {

                PlayerStats stats = new PlayerStats();
                stats.setSeason(season);
                stats.setAppearances(statsNode.path("games").path("appearences").asInt(0));
                stats.setMinutes(statsNode.path("games").path("minutes").asInt(0));
                stats.setGoals(statsNode.path("goals").path("total").asInt(0));
                stats.setAssists(statsNode.path("goals").path("assists").asInt(0));

                String ratingStr = statsNode.path("games").path("rating").asText();
                if (ratingStr != null && !ratingStr.equals("null")) {
                    try {
                        stats.setRating(Double.parseDouble(ratingStr));
                    } catch (NumberFormatException e) { stats.setRating(null); }
                }

                stats.setPlayer(p);
                p.setStats(stats);
                playerRepository.save(p);
                System.out.println("   -> Stats : " + p.getName() + " (" + stats.getGoals() + " buts)");
                break; // Joueur trouvé
            }
        }
    }

    public Long searchTeamId(String teamName) {
        // Nettoyage du nom pour maximiser les chances de correspondance
        String cleanName = teamName.replace(" FC", "").replace("OSC", "").trim();
        String url = apiUrl + "/teams?name=" + cleanName;

        JsonNode root = callApi(url);
        if (root != null && root.path("results").asInt() > 0) {
            return root.path("response").get(0).path("team").path("id").asLong();
        }
        System.out.println("⚠️ Équipe introuvable sur API-Football : " + cleanName);
        return null;
    }

    public JsonNode callApi(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            return null;
        }
    }
}
