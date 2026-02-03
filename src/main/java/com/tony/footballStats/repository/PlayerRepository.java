package com.tony.footballStats.repository;

import com.tony.footballStats.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
