package com.tony.footballStats.controller;

import com.tony.footballStats.model.Team;
import com.tony.footballStats.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @PostMapping
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        return ResponseEntity.ok(teamService.createTeam(team));
    }
}
