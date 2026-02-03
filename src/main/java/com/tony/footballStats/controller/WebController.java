package com.tony.footballStats.controller;

import com.tony.footballStats.model.Player;
import com.tony.footballStats.model.Team;
import com.tony.footballStats.repository.LeagueRepository;
import com.tony.footballStats.repository.PlayerRepository;
import com.tony.footballStats.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class WebController {
    // Repositories
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    // Page d'accueil : Liste des championnats et leurs équipes
    @GetMapping("/")
    public String index(Model model) {
        // On envoie la liste des ligues actives à la vue HTML
        model.addAttribute("leagues", leagueRepository.findByActiveTrue());
        return "index";
    }

    // Page de détail d'une équipe
    @GetMapping("/team/{id}")
    public String teamDetail(@PathVariable Long id, Model model) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team Id:" + id));

        model.addAttribute("team", team);
        return "team";
    }

    @GetMapping("/player/{id}")
    public String playerDetail(@PathVariable Long id, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Joueur introuvable ID:" + id));

        model.addAttribute("player", player);
        return "player";
    }
}
