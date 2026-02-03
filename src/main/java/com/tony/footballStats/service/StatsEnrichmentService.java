package com.tony.footballStats.service;

import com.tony.footballStats.model.Team;
import com.tony.footballStats.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsEnrichmentService {
    // Repositories
    private final TeamRepository teamRepository;
    // Services
    private final PlayerStatsService playerStatsService;

    // Se lance toutes les 2 minutes pour tester (changez en 3600000 pour 1h)
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void enrichNextTeam() {
        System.out.println("🕵️ Recherche de statistiques manquantes...");

        List<Team> teams = teamRepository.findAll();

        for (Team team : teams) {
            // On vérifie si l'équipe a déjà des stats pour éviter de gaspiller des appels
            boolean hasStats = team.getPlayers().stream().anyMatch(p -> p.getStats() != null);

            if (!hasStats) {
                System.out.println("⚡ Enrichissement en cours pour : " + team.getName());
                playerStatsService.fetchAndSaveStats(team);
                return; // On s'arrête après UNE équipe pour respecter les limites API
            }
        }
        System.out.println("💤 Aucune équipe à enrichir pour le moment.");
    }
}
