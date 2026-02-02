package com.tony.footballStats.config;

import com.tony.footballStats.model.League;
import com.tony.footballStats.repository.LeagueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final LeagueRepository leagueRepository;

    public DataInitializer(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    @Override
    public void run(String... args) {
        // Liste des codes gratuits sur football-data.org
        List<LeagueInitData> leagues = Arrays.asList(
                new LeagueInitData("FL1", "Ligue 1", "France"),
                new LeagueInitData("PL", "Premier League", "England"),
                new LeagueInitData("ELC", "Championship", "England"),
                new LeagueInitData("SA", "Serie A", "Italy"),
                new LeagueInitData("PD", "La Liga", "Spain"),
                new LeagueInitData("BL1", "Bundesliga", "Germany"),
                new LeagueInitData("PPL", "Primeira Liga", "Portugal"),
                new LeagueInitData("DED", "Eredivisie", "Netherlands"),
                new LeagueInitData("CL", "Champions League", "Europe")
        );

        for (LeagueInitData data : leagues) {
            if (leagueRepository.findByCode(data.code).isEmpty()) {
                League league = new League();
                league.setCode(data.code);
                league.setName(data.name);
                league.setCountry(data.country);
                league.setActive(true); // On active par défaut
                leagueRepository.save(league);
                System.out.println("✅ Ligue initialisée : " + data.name);
            }
        }
    }

    // Petite classe interne pour structurer les données proprement
    record LeagueInitData(String code, String name, String country) {}
}
