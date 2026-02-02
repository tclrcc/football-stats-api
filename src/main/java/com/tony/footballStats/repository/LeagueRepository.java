package com.tony.footballStats.repository;

import com.tony.footballStats.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {
    // Récupérer uniquement les ligues qu'on a décidé d'activer
    List<League> findByActiveTrue();

    // Utile pour vérifier si elle existe déjà avant d'insérer
    Optional<League> findByCode(String code);
}
